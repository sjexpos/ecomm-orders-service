package io.oigres.ecomm.service.orders.model;

public class NotFoundException extends BusinessApiException {
    public NotFoundException() {}
    public NotFoundException(String message) {super(message);}
}
