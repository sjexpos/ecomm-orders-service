package io.oigres.ecomm.service.orders.api;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.orders.model.order.OrdersCountResponse;

public interface AsyncOrdersService {
    Future<OrdersCountResponse> getOrdersCountAsync(String status);
}
