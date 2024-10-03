package io.oigres.ecomm.service.orders.model.cart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class InsertCartResponseTest {

    @Test
    void check_get_and_set() {

        InsertCartResponse response = InsertCartResponse.builder()
                .orders(List.of(
                        InsertOrderResponse.builder()
                                .orderId(123L)
                                .status("DELIVERED")
                                .total(BigDecimal.valueOf(2.5))
                                .build()
                ))
                .build();

        Assertions.assertNotNull(response.getOrders());
        Assertions.assertEquals(1, response.getOrders().size());
        Assertions.assertEquals(123L, response.getOrders().get(0).getOrderId());
        Assertions.assertEquals("DELIVERED", response.getOrders().get(0).getStatus());
        Assertions.assertEquals(BigDecimal.valueOf(2.5), response.getOrders().get(0).getTotal());
    }
}