package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.OrderStatus;

@Repository
public interface OrderStatusJpaRepository extends JpaRepository<OrderStatus, Long>, OrderStatusRepository {}
