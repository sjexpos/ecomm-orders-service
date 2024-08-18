package io.oigres.ecomm.service.orders.usecases.carts.create;

import io.oigres.ecomm.service.orders.exception.CreateCartException;
import io.oigres.ecomm.service.orders.model.carts.InsertCartRequestDTO;
import io.oigres.ecomm.service.orders.model.carts.InsertCartResponseDTO;
import io.oigres.ecomm.service.products.model.exception.NoStockException;
import io.oigres.ecomm.service.products.model.exception.NotFoundException;
import io.oigres.ecomm.service.products.model.exception.StockTimeOutException;

public interface CreateCartUseCase {
    InsertCartResponseDTO handle(InsertCartRequestDTO request) throws NotFoundException, StockTimeOutException, NoStockException, io.oigres.ecomm.service.users.api.model.exception.NotFoundException,
            io.oigres.ecomm.service.orders.exception.NotFoundException, CreateCartException;
}
