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

package io.oigres.ecomm.service.orders;

public class Routes {

  public static final String API_PREFIX = "/api";

  public static final String CARTS_CONTROLLER_PATH = API_PREFIX + "/v1/carts";
  public static final String ORDERS_CONTROLLER_PATH = API_PREFIX + "/v1/orders";
  public static final String CRON_JOBS_CONTROLLER_PATH = API_PREFIX + "/v1/cronjobs";

  public static final String GET_ORDER_BY_ID = "/{orderId}";
  public static final String GET_TOTAL_ORDERS = "/total";
  public static final String CHANGE_ORDER_STATUS = "/{orderId}/status";
  public static final String GET_ORDER_STATUS_AMOUNTS = "/status/amounts";
  public static final String GET_ALL_PAYMENT_METHODS = "/payment-methods";
  public static final String GET_ALL_DELIVERY_METHODS = "/delivery-methods";

  private Routes() {}
}
