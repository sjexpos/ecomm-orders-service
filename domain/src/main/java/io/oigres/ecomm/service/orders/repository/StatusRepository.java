package io.oigres.ecomm.service.orders.repository;

import java.util.List;
import java.util.Optional;

import io.oigres.ecomm.service.orders.domain.Status;
import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;

public interface StatusRepository extends GenericRepository<Status, Long> {
    Optional<Status> findById(long id);

    Optional<Status> findByName(OrderStatusEnum name);

    List<Status> findAll();
}
