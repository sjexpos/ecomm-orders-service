package io.oigres.ecomm.service.orders.repository;

import io.oigres.ecomm.service.orders.domain.OrderStatus;

public interface OrderStatusRepository extends GenericRepository<OrderStatus, Long> {
    OrderStatus save(OrderStatus orderStatus);
}
