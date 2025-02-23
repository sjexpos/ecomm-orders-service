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
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.order.*;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

public class OrdersServiceProxy extends MiddlewareProxy
    implements OrdersService, AsyncOrdersService {

  public OrdersServiceProxy(WebClient webClient) {
    super(webClient);
  }

  public OrdersServiceProxy(final String baseUri) {
    super(baseUri, Duration.ofMillis(2000));
  }

  // --------------------------------- getOrderById --------------------------------- //

  private Function<UriBuilder, URI> getOrderById_Call(long orderId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_ORDER_BY_ID))
            .build(orderId);
  }

  @Override
  public GetOrderByIdResponse getOrderById(long orderId) {
    return get(getOrderById_Call(orderId), GetOrderByIdResponse.class);
  }

  // --------------------------------- getAllOrders --------------------------------- //

  @Override
  public PageResponse<GetAllOrdersResponse> getAllOrders(
      Long dispensaryId, Long userId, OrderStatusEnumApi status, PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.ORDERS_CONTROLLER_PATH)
                .queryParamIfPresent("dispensaryId", Optional.ofNullable(dispensaryId))
                .queryParamIfPresent("userId", Optional.ofNullable(userId))
                .queryParamIfPresent("status", Optional.ofNullable(status))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(dispensaryId, userId, status, pageable),
        GetAllOrdersResponse.class);
  }

  // --------------------------------- getOrdersCount --------------------------------- //

  private Function<UriBuilder, URI> getOrdersCount_Call(
      Long dispensaryId, Long userId, OrderStatusEnumApi status) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_TOTAL_ORDERS))
            .queryParamIfPresent("dispensaryId", Optional.ofNullable(dispensaryId))
            .queryParamIfPresent("userId", Optional.ofNullable(userId))
            .queryParamIfPresent("status", Optional.ofNullable(status))
            .build(status);
  }

  @Override
  public OrdersCountResponse getOrdersCount(
      Long dispensaryId, Long userId, OrderStatusEnumApi status) {
    return get(getOrdersCount_Call(dispensaryId, userId, status), OrdersCountResponse.class);
  }

  @Override
  public CompletableFuture<OrdersCountResponse> getOrdersCountAsync(
      Long dispensaryId, Long userId, OrderStatusEnumApi status) {
    return getAsync(getOrdersCount_Call(dispensaryId, userId, status), OrdersCountResponse.class);
  }

  // --------------------------------- changeOrderStatus --------------------------------- //

  @Override
  public ChangeCurrentOrderStatusResponse changeOrderStatus(
      long orderId, ChangeCurrentOrderStatusRequest request) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.CHANGE_ORDER_STATUS))
                .build(orderId),
        request,
        ChangeCurrentOrderStatusResponse.class);
  }

  // --------------------------------- getOrdersStatusAmount --------------------------------- //

  private Function<UriBuilder, URI> getOrdersStatusAmount_Call(Long dispensaryId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.ORDERS_CONTROLLER_PATH.concat(Routes.GET_ORDER_STATUS_AMOUNTS))
            .build(dispensaryId);
  }

  @Override
  public OrderStatusAmountsResponse getOrdersStatusAmount(Long dispensaryId) {
    return get(getOrdersStatusAmount_Call(dispensaryId), OrderStatusAmountsResponse.class);
  }
}
