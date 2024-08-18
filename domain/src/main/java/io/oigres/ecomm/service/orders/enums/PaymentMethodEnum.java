package io.oigres.ecomm.service.orders.enums;

import java.util.Optional;

public enum PaymentMethodEnum {
    CASH(1, "Cash");

    private final Integer id;
    private final String prettyName;

    PaymentMethodEnum(Integer id, String prettyName) {
        this.id = id;
        this.prettyName = prettyName;
    }

    public Integer getId() {
        return id;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public static Optional<PaymentMethodEnum> getByName(String name) {
        for (PaymentMethodEnum e : values()) {
            if (e.getPrettyName().equalsIgnoreCase(name) || e.name().equalsIgnoreCase(name)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
