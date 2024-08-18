package io.oigres.ecomm.service.orders.enums;

import java.util.Optional;

public enum DeliveryMethodEnum {
    PICK_UP(1, "Pick Up");

    private final Integer id;
    private final String prettyName;

    DeliveryMethodEnum(Integer id, String prettyName) {
        this.id = id;
        this.prettyName = prettyName;
    }

    public Integer getId() {
        return id;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public static Optional<DeliveryMethodEnum> getByName(String name) {
        for (DeliveryMethodEnum e : values()) {
            if (e.getPrettyName().equalsIgnoreCase(name) || e.name().equalsIgnoreCase(name)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
