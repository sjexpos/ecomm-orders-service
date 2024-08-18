package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.OrderProduct;
import io.oigres.ecomm.service.orders.repository.OrderProductRepository;

@Repository
public interface OrderProductJpaRepository extends JpaRepository<OrderProduct, Long>, OrderProductRepository {}
