package io.oigres.ecomm.service.orders.exception;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() { super("Order not found"); }
}