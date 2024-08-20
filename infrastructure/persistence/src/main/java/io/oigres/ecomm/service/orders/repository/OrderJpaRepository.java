package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.Order;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;

import java.math.BigDecimal;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepository {
    @Override
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.lastStatus = :status")
    BigDecimal sumOrdersTotalsByLastStatus(@Param("status") OrderStatusEnum status);

    @Override
    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    BigDecimal sumOrdersTotals();
}
