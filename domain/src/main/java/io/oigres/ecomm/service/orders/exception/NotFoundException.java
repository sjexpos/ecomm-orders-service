package io.oigres.ecomm.service.orders.exception;

import io.oigres.ecomm.service.orders.domain.DomainException;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {super(message);}
}
