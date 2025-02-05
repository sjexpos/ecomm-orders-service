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

package io.oigres.ecomm.service.orders.config;

import io.oigres.ecomm.service.orders.rpc.StockTransactionsServiceProxyEnhanced;
import io.oigres.ecomm.service.orders.rpc.UsersServiceProxyEnhanced;
import io.oigres.ecomm.service.products.api.StockTransactionServiceProxy;
import io.oigres.ecomm.service.products.model.StockTransactionsService;
import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.UsersServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RemoteServicesConfiguration {

  @Bean
  public StockTransactionsService stockTransactionService(
      WebClient.Builder builder,
      @Value("${ecomm.service.products.baseUri}") String productsBaseUri) {
    return new StockTransactionsServiceProxyEnhanced(
        new StockTransactionServiceProxy(builder.baseUrl(productsBaseUri).build()));
  }

  @Bean
  public UsersService asyncConsumerUsersService(
      WebClient.Builder builder, @Value("${ecomm.service.users.baseUri}") String usersBaseUri) {
    return new UsersServiceProxyEnhanced(
        new UsersServiceProxy(builder.baseUrl(usersBaseUri).build()));
  }
}
