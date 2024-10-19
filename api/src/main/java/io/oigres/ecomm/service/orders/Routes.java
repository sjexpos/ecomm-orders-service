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
