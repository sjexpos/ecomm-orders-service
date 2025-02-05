/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.model.cart;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InsertCartResponseTest {

  @Test
  void check_get_and_set() {

    InsertCartResponse response =
        InsertCartResponse.builder()
            .orders(
                List.of(
                    InsertOrderResponse.builder()
                        .orderId(123L)
                        .status("DELIVERED")
                        .total(BigDecimal.valueOf(2.5))
                        .build()))
            .build();

    Assertions.assertNotNull(response.getOrders());
    Assertions.assertEquals(1, response.getOrders().size());
    Assertions.assertEquals(123L, response.getOrders().get(0).getOrderId());
    Assertions.assertEquals("DELIVERED", response.getOrders().get(0).getStatus());
    Assertions.assertEquals(BigDecimal.valueOf(2.5), response.getOrders().get(0).getTotal());
  }
}
