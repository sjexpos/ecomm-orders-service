package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.PaymentMethod;
import io.oigres.ecomm.service.orders.enums.PaymentMethodEnum;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends GenericRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findById(long id);

    Optional<PaymentMethod> findByName(PaymentMethodEnum name);

    Page<PaymentMethod> findAll(Pageable pageable);
}
