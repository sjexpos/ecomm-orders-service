package io.oigres.ecomm.service.orders.api;

public class RemoteServiceUnavailableException extends RuntimeException {

    public RemoteServiceUnavailableException(String message) {
        super(message);
    }

}
