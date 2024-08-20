package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderRepository extends GenericRepository<Order, Long> {
    Optional<Order> findById(long id);

    Optional<Order> findByIdAndDispensaryId(long id, long dispensaryId);

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    Page<Order> findAllByDispensaryId(long dispensaryId, Pageable pageable);

    Page<Order> findAllByDispensaryIdAndLastStatus(long dispensaryId, OrderStatusEnum lastStatus, Pageable pageable);

    Page<Order> findAllByLastStatus(OrderStatusEnum lastStatus, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    BigDecimal sumOrdersTotalsByLastStatus(OrderStatusEnum status);

    BigDecimal sumOrdersTotals();

    Order save(Order order);

    Long countByLastStatusAndDispensaryId(OrderStatusEnum status, Long dispensaryId);

    Long countByLastStatus(OrderStatusEnum status);
}
