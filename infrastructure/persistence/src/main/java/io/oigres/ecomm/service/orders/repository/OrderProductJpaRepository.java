package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.OrderProduct;

@Repository
public interface OrderProductJpaRepository extends JpaRepository<OrderProduct, Long>, OrderProductRepository {}
