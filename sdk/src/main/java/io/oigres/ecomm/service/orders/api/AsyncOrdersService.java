package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.OrderStatusEnumApi;
import io.oigres.ecomm.service.orders.model.order.OrdersCountResponse;
import reactor.core.publisher.Mono;

public interface AsyncOrdersService {

    Mono<OrdersCountResponse> getOrdersCountAsync(Long dispensaryId, Long userId, OrderStatusEnumApi status);

}
