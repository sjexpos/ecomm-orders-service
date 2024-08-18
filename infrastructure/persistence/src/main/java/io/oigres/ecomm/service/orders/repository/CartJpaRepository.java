package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.Cart;
import io.oigres.ecomm.service.orders.repository.CartRepository;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, Long>, CartRepository {}
