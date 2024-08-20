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
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public CreateCartUseCaseImpl(StockTransactionsService stockTransactionsService, UsersService usersService, CartRepository cartRepository, OrderRepository orderRepository,
                                 OrderStatusRepository orderStatusRepository, OrderProductRepository orderProductRepository, PaymentMethodRepository paymentMethodRepository,
                                 DeliveryMethodRepository deliveryMethodRepository, StatusRepository statusRepository) {
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
            throws NotFoundException, StockTimeOutException, NoStockException, io.oigres.ecomm.service.users.api.model.exception.NotFoundException,
            io.oigres.ecomm.service.orders.exception.NotFoundException, CreateCartException {
        InsertStockTransactionResponse response = stockTransactionsService.insertStockTransactions(InsertStockTransactionRequest.builder().publications(
                request.getPublications().stream().map(pub -> PublicationWithAmountRequest.builder().publicationId(pub.getPublicationId()).amount(pub.getAmount()).build())
                        .collect(Collectors.toList())).build());
        try {
            PaymentMethod paymentMethod =
                    paymentMethodRepository.findByName(PaymentMethodEnum.CASH).orElseThrow(() -> new io.oigres.ecomm.service.orders.exception.NotFoundException("Payment method not found"));
            DeliveryMethod deliveryMethod =
                    deliveryMethodRepository.findByName(DeliveryMethodEnum.PICK_UP).orElseThrow(() -> new io.oigres.ecomm.service.orders.exception.NotFoundException("Delivery method not found"));
            Status status = statusRepository.findByName(OrderStatusEnum.ORDERED).orElseThrow(() -> new io.oigres.ecomm.service.orders.exception.NotFoundException("Status not found"));
            GetConsumerStateTax tax = usersService.getStateTaxByUserId(request.getUserId());
            Map<Long, BigDecimal> commissionPerDispensary = response.getTransactions().stream().map(tr -> Pair.of(tr.getPublicationDispensaryId(), tr.getDispensaryCommission())).distinct()
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            Map<Long, List<PublicationWithTransactionResponse>> publicationsPerDispensary =
                    response.getTransactions().stream().collect(Collectors.groupingBy(PublicationWithTransactionResponse::getPublicationDispensaryId));
            Cart cart = Cart.builder().userId(request.getUserId()).date(LocalDateTime.now()).build();
            cart = cartRepository.save(cart);
            List<Order> orders = new ArrayList<>();
            for (Long dispensaryId : publicationsPerDispensary.keySet()) {
                BigDecimal subtotal = BigDecimal.ZERO;
                List<PublicationWithTransactionResponse> publications = publicationsPerDispensary.get(dispensaryId);
                Set<OrderProduct> products = new HashSet<>();
                for (PublicationWithTransactionResponse transaction : publications) {
                    OrderProduct orderProduct =
                            OrderProduct.builder().dispensaryProductId(transaction.getPublicationId()).units(transaction.getReservedAmount()).price(transaction.getPublicationPrice())
                                    .transactionId(transaction.getTransactionId()).build();
                    subtotal = subtotal.add(transaction.getPublicationPrice().multiply(BigDecimal.valueOf(transaction.getReservedAmount())));
                    products.add(orderProduct);
                }
                BigDecimal exciseTax = subtotal.multiply(tax.getTax());
                BigDecimal salesTax = subtotal.multiply(commissionPerDispensary.get(dispensaryId));
                BigDecimal total = subtotal.add(exciseTax).add(salesTax);
                OrderStatus orderStatus = OrderStatus.builder().date(LocalDateTime.now()).status(status).build();
                Order order = Order.builder().userId(request.getUserId()).dispensaryId(dispensaryId).cart(cart).date(LocalDateTime.now()).paymentMethod(paymentMethod).deliveryMethod(deliveryMethod)
                        .orderStatuses(Set.of(orderStatus)).orderProducts(products).subtotalPrice(subtotal).exciseTax(exciseTax).salesTax(salesTax).totalPrice(total)
                        .lastStatus(OrderStatusEnum.ORDERED).build();
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
                    ConfirmStockTransactionRequest.builder().transactionIds(response.getTransactions().stream().map(PublicationWithTransactionResponse::getTransactionId).collect(Collectors.toList()))
                            .build());
            return InsertCartResponseDTO.builder().orders(orders.stream().map(or -> InsertOrderResponseDTO.builder().orderId(or.getId()).dispensaryId(or.getDispensaryId()).publications(
                                    or.getOrderProducts().stream()
                                            .map(pub -> InsertOrderPublicationResponseDTO.builder().publicationId(pub.getDispensaryProductId()).amount(pub.getUnits()).price(pub.getPrice()).build())
                                            .collect(Collectors.toList())).deliveryMethod(or.getDeliveryMethod().getName().getPrettyName()).paymentMethod(or.getPaymentMethod().getName().getPrettyName())
                            .status(or.getLastStatus().name()).subtotal(or.getSubtotalPrice()).exciseTax(or.getExciseTax()).salesTax(or.getSalesTax()).total(or.getTotalPrice()).build())
                                                                  .collect(Collectors.toList())).build();
        } catch (Exception e) {
            stockTransactionsService.revertStockTransactions(
                    RevertStockTransactionRequest.builder().transactionIds(response.getTransactions().stream().map(PublicationWithTransactionResponse::getTransactionId).collect(Collectors.toList()))
                            .build());
            throw new CreateCartException(e.getMessage());
        }
    }
}
