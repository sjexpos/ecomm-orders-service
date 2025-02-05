/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.OrderStatusEnumApi;
import io.oigres.ecomm.service.orders.OrdersService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.domain.OrderStatus;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.exception.OrderNotFoundException;
import io.oigres.ecomm.service.orders.model.NotFoundException;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageResponseImpl;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.order.*;
import io.oigres.ecomm.service.orders.usecases.orders.changeStatus.ChangeOrderStatusUseCase;
import io.oigres.ecomm.service.orders.usecases.orders.list.GetAllOrdersUseCase;
import io.oigres.ecomm.service.orders.usecases.orders.search.GetOrderByIdUseCase;
import io.oigres.ecomm.service.orders.usecases.orders.search.GetOrderStatusAmountsUseCase;
import io.oigres.ecomm.service.orders.usecases.orders.total.GetOrdersCountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.ORDERS_CONTROLLER_PATH)
@Tag(name = "Orders", description = " ")
@Slf4j
@Validated
public class OrdersController extends AbstractController implements OrdersService {
  private final GetOrderByIdUseCase getOrderByIdUseCase;
  private final GetAllOrdersUseCase getAllOrdersUseCase;
  private final GetOrdersCountUseCase getOrdersCountUseCase;
  private final ChangeOrderStatusUseCase changeOrderStatusUseCase;
  private final GetOrderStatusAmountsUseCase getOrderStatusAmountsUseCase;

  public OrdersController(
      GetOrderByIdUseCase getOrderByIdUseCase,
      GetAllOrdersUseCase getAllOrdersUseCase,
      GetOrdersCountUseCase getOrdersCountUseCase,
      ChangeOrderStatusUseCase changeOrderStatusUseCase,
      GetOrderStatusAmountsUseCase getOrderStatusAmountsUseCase) {
    this.getOrderByIdUseCase = getOrderByIdUseCase;
    this.getAllOrdersUseCase = getAllOrdersUseCase;
    this.getOrdersCountUseCase = getOrdersCountUseCase;
    this.changeOrderStatusUseCase = changeOrderStatusUseCase;
    this.getOrderStatusAmountsUseCase = getOrderStatusAmountsUseCase;
  }

  @Override
  @Operation(summary = "Get order by id")
  @GetMapping(value = Routes.GET_ORDER_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public GetOrderByIdResponse getOrderById(
      @Parameter(
              name = "orderId",
              required = true,
              description = "identifier associated with the order (1..N)")
          @Min(value = 1, message = "orderId should be greater than zero")
          @PathVariable(name = "orderId")
          long orderId)
      throws NotFoundException {
    log.debug("############ call getOrderById ############");
    try {
      Order order = getOrderByIdUseCase.handle(orderId);
      return GetOrderByIdResponse.builder()
          .id(order.getId())
          .orderDate(order.getDate())
          .userId(order.getUserId())
          .dispensaryId(order.getDispensaryId())
          .deliveryMethod(order.getDeliveryMethod().getName().getPrettyName())
          .paymentMethod(order.getPaymentMethod().getName().getPrettyName())
          .subtotal(order.getSubtotalPrice())
          .exciseTax(order.getExciseTax())
          .salesTax(order.getSalesTax())
          .total(order.getTotalPrice())
          .items(
              order.getOrderProducts().stream()
                  .map(
                      itm ->
                          OrderPublicationResponse.builder()
                              .publicationId(itm.getDispensaryProductId())
                              .price(itm.getPrice())
                              .units(itm.getUnits())
                              .build())
                  .collect(Collectors.toList()))
          .statuses(
              order.getOrderStatuses().stream()
                  .sorted(Comparator.comparing(OrderStatus::getDate))
                  .map(
                      sts ->
                          GetStatusesResponse.builder()
                              .date(sts.getDate())
                              .status(sts.getStatus().getName().name())
                              .build())
                  .collect(Collectors.toList()))
          .build();
    } catch (OrderNotFoundException e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  @Override
  @Operation(summary = "Get all orders")
  @PageableAsQueryParam
  @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public PageResponse<GetAllOrdersResponse> getAllOrders(
      @RequestParam(name = "dispensaryId", required = false) Long dispensaryId,
      @RequestParam(name = "userId", required = false) Long userId,
      @RequestParam(name = "status", required = false) OrderStatusEnumApi status,
      @Parameter(hidden = true, required = true) PageableRequest pageable) {
    log.debug("############ call getAllOrders ############");
    Page<Order> orders;
    try {
      OrderStatusEnum orderStatusEnum =
          status != null ? OrderStatusEnum.valueOf(status.name()) : null;
      orders =
          getAllOrdersUseCase.handle(
              dispensaryId,
              userId,
              orderStatusEnum,
              PageRequest.of(
                  pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
    } catch (io.oigres.ecomm.service.orders.exception.NotFoundException e) {
      return PageResponse.empty(pageable);
    }

    List<GetAllOrdersResponse> response =
        orders.getContent().stream()
            .map(
                or -> {
                  OrderStatus sts =
                      or.getOrderStatuses().stream()
                          .max(Comparator.comparing(OrderStatus::getDate))
                          .get();
                  return GetAllOrdersResponse.builder()
                      .id(or.getId())
                      .orderDate(or.getDate())
                      .orderStatus(sts.getStatus().getName().name())
                      .orderStatusDate(sts.getDate())
                      .dispensaryId(or.getDispensaryId())
                      .userId(or.getUserId())
                      .total(or.getTotalPrice())
                      .items(
                          or.getOrderProducts().stream()
                              .map(
                                  pub ->
                                      OrderPublicationResponse.builder()
                                          .publicationId(pub.getDispensaryProductId())
                                          .units(pub.getUnits())
                                          .price(pub.getPrice())
                                          .build())
                              .collect(Collectors.toList()))
                      .build();
                })
            .collect(Collectors.toList());

    return new PageResponseImpl<>(response, pageable, orders.getTotalElements());
  }

  @Override
  @Operation(summary = "Get total from all orders filtered by status")
  @PageableAsQueryParam
  @GetMapping(value = Routes.GET_TOTAL_ORDERS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public OrdersCountResponse getOrdersCount(
      @RequestParam(name = "dispensaryId", required = false) Long dispensaryId,
      @RequestParam(name = "userId", required = false) Long userId,
      @RequestParam(name = "status", required = false) OrderStatusEnumApi status) {
    log.debug("############ call getOrdersCount ############");
    BigDecimal total;
    try {
      total = getOrdersCountUseCase.getOrdersCount(status.name());
    } catch (io.oigres.ecomm.service.orders.exception.NotFoundException e) {
      return OrdersCountResponse.builder().total(BigDecimal.ZERO).build();
    }
    return OrdersCountResponse.builder().total(total).build();
  }

  @Override
  @Operation(summary = "Change order status")
  @PageableAsQueryParam
  @PatchMapping(value = Routes.CHANGE_ORDER_STATUS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ChangeCurrentOrderStatusResponse changeOrderStatus(
      @Parameter(
              name = "orderId",
              required = true,
              description = "identifier associated with the order (1..N)")
          @Min(value = 1, message = "orderId should be greater than zero")
          @PathVariable(name = "orderId")
          long orderId,
      @RequestBody @Valid ChangeCurrentOrderStatusRequest request)
      throws NotFoundException {
    log.debug("############ call changeOrderStatus ############");
    Order order;
    try {
      order = changeOrderStatusUseCase.handle(orderId, request.getDispensaryId());
    } catch (io.oigres.ecomm.service.orders.exception.NotFoundException e) {
      throw new NotFoundException(e.getMessage());
    }
    return ChangeCurrentOrderStatusResponse.builder()
        .currentStatus(order.getLastStatus().name())
        .statuses(
            order.getOrderStatuses().stream()
                .sorted(Comparator.comparing(OrderStatus::getDate))
                .map(
                    sts ->
                        GetStatusesResponse.builder()
                            .date(sts.getDate())
                            .status(sts.getStatus().getName().name())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }

  @Override
  @Operation(summary = "Get orders status amount")
  @GetMapping(
      value = Routes.GET_ORDER_STATUS_AMOUNTS,
      produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public OrderStatusAmountsResponse getOrdersStatusAmount(
      @RequestParam(name = "dispensaryId", required = false) Long dispensaryId) {
    log.debug("############ call getOrdersStatusAmount ############");
    OrderStatusAmountsResponse response = new OrderStatusAmountsResponse();
    OrderStatusEnumApi.stream()
        .forEach(
            status -> {
              Long amount = null;
              try {
                amount =
                    getOrderStatusAmountsUseCase.getOrderStatusAmount(
                        OrderStatusEnum.valueOf(status.name()), dispensaryId);
              } catch (io.oigres.ecomm.service.orders.exception.NotFoundException e) {
                log.error("Order status amount by {} status was throwing an error", status);
                log.error(e.getMessage());
              }
              response
                  .getStatus()
                  .add(OrderStatusAmountDto.builder().name(status).amount(amount).build());
            });
    return response;
  }
}
