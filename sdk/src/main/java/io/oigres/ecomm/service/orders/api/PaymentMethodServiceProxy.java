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

import io.oigres.ecomm.service.orders.PaymentMethodsService;
import io.oigres.ecomm.service.orders.Routes;
import io.oigres.ecomm.service.orders.model.JacksonPageImpl;
import io.oigres.ecomm.service.orders.model.PageResponse;
import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.cart.GetAllPaymentMethodsResponse;
import java.time.Duration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class PaymentMethodServiceProxy extends MiddlewareProxy implements PaymentMethodsService {
  public PaymentMethodServiceProxy(WebClient webClient) {
    super(webClient);
  }

  public PaymentMethodServiceProxy(final String baseUri) {
    super(baseUri, Duration.ofMillis(2000));
  }

  @Override
  public PageResponse<GetAllPaymentMethodsResponse> getAllPaymentMethods(PageableRequest pageable) {
    ParameterizedTypeReference<JacksonPageImpl<GetAllPaymentMethodsResponse>> typeReference =
        new ParameterizedTypeReference<>() {};
    return getWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(Routes.CARTS_CONTROLLER_PATH.concat(Routes.GET_ALL_PAYMENT_METHODS))
                    .queryParam("page", pageable.getPageNumber())
                    .queryParam("size", pageable.getPageSize())
                    .build(pageable))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(typeReference)
        .block();
  }
}
