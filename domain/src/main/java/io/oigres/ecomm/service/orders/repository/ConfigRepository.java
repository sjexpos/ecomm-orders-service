package io.oigres.ecomm.service.orders.repository;

import java.util.List;
import java.util.Optional;

import io.oigres.ecomm.service.orders.domain.Config;

public interface ConfigRepository extends GenericRepository<Config, Long> {
    Optional<Config> findById(long id);

    Optional<Config> findByKey(String key);

    List<Config> findAll();
}
