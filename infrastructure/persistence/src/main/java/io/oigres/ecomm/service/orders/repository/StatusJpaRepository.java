package io.oigres.ecomm.service.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.orders.domain.Status;
import io.oigres.ecomm.service.orders.repository.StatusRepository;

@Repository
public interface StatusJpaRepository extends JpaRepository<Status, Long>, StatusRepository {}
