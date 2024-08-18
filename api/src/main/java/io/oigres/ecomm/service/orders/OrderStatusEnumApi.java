package io.oigres.ecomm.service.orders;

import java.util.stream.Stream;

public enum OrderStatusEnumApi {
    ORDERED,
    CONFIRMED,
    READY,
    DELIVERED,
    CANCELED;


    public static Stream<OrderStatusEnumApi> stream() {
        return Stream.of(OrderStatusEnumApi.values());
    }
}
