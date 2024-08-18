package io.oigres.ecomm.service.orders.repository;

import java.util.List;
import java.util.Optional;

import io.oigres.ecomm.service.orders.domain.Cart;

public interface CartRepository extends GenericRepository<Cart, Long> {
    Optional<Cart> findById(long id);

    List<Cart> findAllByUserId(long userId);

    Cart save(Cart cart);
}
