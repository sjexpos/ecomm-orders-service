package io.oigres.ecomm.service.orders.usecases.orders.total;

import java.math.BigDecimal;

import io.oigres.ecomm.service.orders.exception.NotFoundException;

public interface GetOrdersCountUseCase {
    BigDecimal getOrdersCount(String status) throws NotFoundException;
}
