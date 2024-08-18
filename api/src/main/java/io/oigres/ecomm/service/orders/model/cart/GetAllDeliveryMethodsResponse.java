package io.oigres.ecomm.service.orders.model.cart;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GetAllDeliveryMethodsResponse implements Serializable {
    private Long id;
    private String name;
}
