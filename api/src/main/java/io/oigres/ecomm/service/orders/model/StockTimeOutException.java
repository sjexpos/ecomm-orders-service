package io.oigres.ecomm.service.orders.model;

public class StockTimeOutException extends BusinessApiException {
    public StockTimeOutException() {}
    public StockTimeOutException(String message) {super(message);}
}
