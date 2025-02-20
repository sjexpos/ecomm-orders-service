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

package io.oigres.ecomm.service.orders.usecases.carts.create;

import io.oigres.ecomm.service.orders.domain.*;
import io.oigres.ecomm.service.orders.enums.DeliveryMethodEnum;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;
import io.oigres.ecomm.service.orders.enums.PaymentMethodEnum;
import io.oigres.ecomm.service.orders.exception.CreateCartException;
import io.oigres.ecomm.service.orders.model.carts.InsertCartRequestDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertCartResponseDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertOrderPublicationResponseDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertOrderResponseDTO;
import io.oigres.ecomm.service.orders.repository.*;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.products.model.exception.NoStockException;
import io.oigres.ecomm.service.products.model.exception.NotFoundException;
import io.oigres.ecomm.service.products.model.exception.StockTimeOutException;
import io.oigres.ecomm.service.products.model.stockTransactions.*;
import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.model.consumer.GetConsumerStateTax;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class CreateCartUseCaseImpl implements CreateCartUseCase {
  private final StockTransactionsService stockTransactionsService;
  private final UsersService usersService;
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final OrderStatusRepository orderStatusRepository;
  private final OrderProductRepository orderProductRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final DeliveryMethodRepository deliveryMethodRepository;
  private final StatusRepository statusRepository;

  public CreateCartUseCaseImpl(
      StockTransactionsService stockTransactionsService,
      UsersService usersService,
      CartRepository cartRepository,
      OrderRepository orderRepository,
      OrderStatusRepository orderStatusRepository,
      OrderProductRepository orderProductRepository,
      PaymentMethodRepository paymentMethodRepository,
      DeliveryMethodRepository deliveryMethodRepository,
      StatusRepository statusRepository) {
    this.stockTransactionsService = stockTransactionsService;
    this.usersService = usersService;
    this.cartRepository = cartRepository;
    this.orderRepository = orderRepository;
    this.orderStatusRepository = orderStatusRepository;
    this.orderProductRepository = orderProductRepository;
    this.paymentMethodRepository = paymentMethodRepository;
    this.deliveryMethodRepository = deliveryMethodRepository;
    this.statusRepository = statusRepository;
  }

  @Override
  @Transactional
  public InsertCartResponseDTO handle(InsertCartRequestDTO request)
      throws NotFoundException,
          StockTimeOutException,
          NoStockException,
          io.oigres.ecomm.service.users.api.model.exception.NotFoundException,
          io.oigres.ecomm.service.orders.exception.NotFoundException,
          CreateCartException {
    InsertStockTransactionResponse response =
        stockTransactionsService.insertStockTransactions(
            InsertStockTransactionRequest.builder()
                .publications(
                    request.getPublications().stream()
                        .map(
                            pub ->
                                PublicationWithAmountRequest.builder()
                                    .publicationId(pub.getPublicationId())
                                    .amount(pub.getAmount())
                                    .build())
                        .collect(Collectors.toList()))
                .build());
    try {
      PaymentMethod paymentMethod =
          paymentMethodRepository
              .findByName(PaymentMethodEnum.CASH)
              .orElseThrow(
                  () ->
                      new io.oigres.ecomm.service.orders.exception.NotFoundException(
                          "Payment method not found"));
      DeliveryMethod deliveryMethod =
          deliveryMethodRepository
              .findByName(DeliveryMethodEnum.PICK_UP)
              .orElseThrow(
                  () ->
                      new io.oigres.ecomm.service.orders.exception.NotFoundException(
                          "Delivery method not found"));
      Status status =
          statusRepository
              .findByName(OrderStatusEnum.ORDERED)
              .orElseThrow(
                  () ->
                      new io.oigres.ecomm.service.orders.exception.NotFoundException(
                          "Status not found"));
      GetConsumerStateTax tax = usersService.getStateTaxByUserId(request.getUserId());
      Map<Long, BigDecimal> commissionPerDispensary =
          response.getTransactions().stream()
              .map(tr -> Pair.of(tr.getPublicationDispensaryId(), tr.getDispensaryCommission()))
              .distinct()
              .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
      Map<Long, List<PublicationWithTransactionResponse>> publicationsPerDispensary =
          response.getTransactions().stream()
              .collect(
                  Collectors.groupingBy(
                      PublicationWithTransactionResponse::getPublicationDispensaryId));
      Cart cart = Cart.builder().userId(request.getUserId()).date(LocalDateTime.now()).build();
      cart = cartRepository.save(cart);
      List<Order> orders = new ArrayList<>();
      for (Map.Entry<Long, List<PublicationWithTransactionResponse>> entry :
          publicationsPerDispensary.entrySet()) {
        BigDecimal subtotal = BigDecimal.ZERO;
        Set<OrderProduct> products = new HashSet<>();
        for (PublicationWithTransactionResponse transaction : entry.getValue()) {
          OrderProduct orderProduct =
              OrderProduct.builder()
                  .dispensaryProductId(transaction.getPublicationId())
                  .units(transaction.getReservedAmount())
                  .price(transaction.getPublicationPrice())
                  .transactionId(transaction.getTransactionId())
                  .build();
          subtotal =
              subtotal.add(
                  transaction
                      .getPublicationPrice()
                      .multiply(BigDecimal.valueOf(transaction.getReservedAmount())));
          products.add(orderProduct);
        }
        BigDecimal exciseTax = subtotal.multiply(tax.getTax());
        BigDecimal salesTax = subtotal.multiply(commissionPerDispensary.get(entry.getKey()));
        BigDecimal total = subtotal.add(exciseTax).add(salesTax);
        OrderStatus orderStatus =
            OrderStatus.builder().date(LocalDateTime.now()).status(status).build();
        Order order =
            Order.builder()
                .userId(request.getUserId())
                .dispensaryId(entry.getKey())
                .cart(cart)
                .date(LocalDateTime.now())
                .paymentMethod(paymentMethod)
                .deliveryMethod(deliveryMethod)
                .orderStatuses(Set.of(orderStatus))
                .orderProducts(products)
                .subtotalPrice(subtotal)
                .exciseTax(exciseTax)
                .salesTax(salesTax)
                .totalPrice(total)
                .lastStatus(OrderStatusEnum.ORDERED)
                .build();
        orders.add(order);
        order = orderRepository.save(order);
        orderStatus.setOrder(order);
        orderStatusRepository.save(orderStatus);
        for (OrderProduct pr : products) {
          pr.setOrder(order);
          orderProductRepository.save(pr);
        }
      }
      stockTransactionsService.confirmStockTransactions(
          ConfirmStockTransactionRequest.builder()
              .transactionIds(
                  response.getTransactions().stream()
                      .map(PublicationWithTransactionResponse::getTransactionId)
                      .collect(Collectors.toList()))
              .build());
      return InsertCartResponseDTO.builder()
          .orders(
              orders.stream()
                  .map(
                      or ->
                          InsertOrderResponseDTO.builder()
                              .orderId(or.getId())
                              .dispensaryId(or.getDispensaryId())
                              .publications(
                                  or.getOrderProducts().stream()
                                      .map(
                                          pub ->
                                              InsertOrderPublicationResponseDTO.builder()
                                                  .publicationId(pub.getDispensaryProductId())
                                                  .amount(pub.getUnits())
                                                  .price(pub.getPrice())
                                                  .build())
                                      .collect(Collectors.toList()))
                              .deliveryMethod(or.getDeliveryMethod().getName().getPrettyName())
                              .paymentMethod(or.getPaymentMethod().getName().getPrettyName())
                              .status(or.getLastStatus().name())
                              .subtotal(or.getSubtotalPrice())
                              .exciseTax(or.getExciseTax())
                              .salesTax(or.getSalesTax())
                              .total(or.getTotalPrice())
                              .build())
                  .collect(Collectors.toList()))
          .build();
    } catch (Exception e) {
      stockTransactionsService.revertStockTransactions(
          RevertStockTransactionRequest.builder()
              .transactionIds(
                  response.getTransactions().stream()
                      .map(PublicationWithTransactionResponse::getTransactionId)
                      .collect(Collectors.toList()))
              .build());
      throw new CreateCartException(e.getMessage());
    }
  }
}
