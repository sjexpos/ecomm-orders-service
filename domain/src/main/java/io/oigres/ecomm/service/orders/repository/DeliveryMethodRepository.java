package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.orders.domain.DeliveryMethod;
import io.oigres.ecomm.service.orders.enums.DeliveryMethodEnum;

import java.util.Optional;

public interface DeliveryMethodRepository extends GenericRepository<DeliveryMethod, Long> {
    Optional<DeliveryMethod> findById(long id);

    Optional<DeliveryMethod> findByName(DeliveryMethodEnum name);

    Page<DeliveryMethod> findAll(Pageable pageable);
}
