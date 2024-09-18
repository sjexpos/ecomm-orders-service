package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.OrderStatusEnumApi;
import io.oigres.ecomm.service.orders.model.order.OrdersCountResponse;

import java.util.concurrent.CompletableFuture;

public interface AsyncOrdersService {

    CompletableFuture<OrdersCountResponse> getOrdersCountAsync(Long dispensaryId, Long userId, OrderStatusEnumApi status);

}
