package io.oigres.ecomm.service.orders;

import jakarta.validation.Valid;

import io.oigres.ecomm.service.orders.model.CreateCartException;
import io.oigres.ecomm.service.orders.model.NoStockException;
import io.oigres.ecomm.service.orders.model.NotFoundException;
import io.oigres.ecomm.service.orders.model.StockTimeOutException;
import io.oigres.ecomm.service.orders.model.cart.InsertCartRequest;
import io.oigres.ecomm.service.orders.model.cart.InsertCartResponse;

public interface CartsService {
    InsertCartResponse createCart(@Valid InsertCartRequest request) throws NotFoundException, NoStockException, StockTimeOutException, CreateCartException;
}
