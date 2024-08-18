package io.oigres.ecomm.service.orders.repository;

import java.util.List;

import io.oigres.ecomm.service.orders.domain.OrderProduct;

public interface OrderProductRepository extends GenericRepository<OrderProduct, Long> {
    OrderProduct save(OrderProduct orderProduct);

    List<OrderProduct> findByTransactionIdIn(List<Long> ids);
}
