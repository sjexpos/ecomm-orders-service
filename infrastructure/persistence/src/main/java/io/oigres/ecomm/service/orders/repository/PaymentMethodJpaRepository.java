package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.PaymentMethod;

@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethod, Long>, PaymentMethodRepository {}
