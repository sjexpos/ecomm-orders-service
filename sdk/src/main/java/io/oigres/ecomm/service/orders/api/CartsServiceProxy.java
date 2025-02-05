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

package io.oigres.ecomm.service.orders.api;

import io.oigres.ecomm.service.orders.CartsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.cart.InsertCartRequest;
import io.oigres.ecomm.service.orders.model.cart.InsertCartResponse;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class CartsServiceProxy extends MiddlewareProxy implements CartsService {

  public CartsServiceProxy(WebClient webClient) {
    super(webClient);
  }

  public CartsServiceProxy(final String baseUri) {
    super(baseUri, Duration.ofMillis(2000));
  }

  @Override
  public InsertCartResponse createCart(InsertCartRequest request) {
    return post(
        uriBuilder -> uriBuilder.path(Routes.CARTS_CONTROLLER_PATH).build(),
        request,
        InsertCartResponse.class);
  }
}
