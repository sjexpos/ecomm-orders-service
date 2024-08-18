package io.oigres.ecomm.service.orders.enums;

import java.util.Optional;

import io.oigres.ecomm.service.orders.exception.NotFoundException;

public enum OrderStatusEnum {
    ORDERED(1L, "Ordered", 2L),
    CONFIRMED(2L, "Confirmed", 3L),
    READY(3L, "Ready", 4L),
    DELIVERED(4L, "Delivered", null),
    CANCELED(5L, "Canceled", null);
    private final Long id;
    private final String prettyName;
    private final Long nextStatusId;

    OrderStatusEnum(Long id, String prettyName, Long nextStatusId) {
        this.id = id;
        this.prettyName = prettyName;
        this.nextStatusId = nextStatusId;
    }

    public Long getId() {
        return id;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public static OrderStatusEnum getNextStatus(OrderStatusEnum statusEnum) throws NotFoundException {
        if (statusEnum.getNextStatusId() == null) {
            throw new NotFoundException(String.format("There is no next status for current status %s", statusEnum.name()));
        } else {
            return getById(statusEnum.getNextStatusId()).get();
        }
    }

    public static Optional<OrderStatusEnum> getByName(String name) {
        for (OrderStatusEnum e : values()) {
            if (e.getPrettyName().equalsIgnoreCase(name) || e.name().equalsIgnoreCase(name)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public static Optional<OrderStatusEnum> getById(Long id) {
        for (OrderStatusEnum e : values()) {
            if (e.getId().equals(id)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Long getNextStatusId() {
        return nextStatusId;
    }
}
