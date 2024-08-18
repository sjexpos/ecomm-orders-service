package io.oigres.ecomm.service.orders.model;

public class NoStockException extends Exception {
    public NoStockException() {}
    public NoStockException(String message) {super(message);}
}
