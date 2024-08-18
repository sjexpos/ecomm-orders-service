package io.oigres.ecomm.service.orders.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertOrderDispensaryPublicationResponse implements Serializable {
    private Long dispensaryPublicationId;
    private BigDecimal price;
    private Integer amount;
}
