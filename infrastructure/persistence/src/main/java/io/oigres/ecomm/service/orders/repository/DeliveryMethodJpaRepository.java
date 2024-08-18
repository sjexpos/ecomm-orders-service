package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.DeliveryMethod;
import io.oigres.ecomm.service.orders.domain.PaymentMethod;
import io.oigres.ecomm.service.orders.repository.DeliveryMethodRepository;

@Repository
public interface DeliveryMethodJpaRepository extends JpaRepository<DeliveryMethod, Long>, DeliveryMethodRepository {}
