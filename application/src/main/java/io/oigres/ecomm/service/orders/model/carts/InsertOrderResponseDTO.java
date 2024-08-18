package io.oigres.ecomm.service.orders.model.carts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertOrderResponseDTO implements Serializable {
    private Long orderId;
    private Long dispensaryId;
    private List<InsertOrderPublicationResponseDTO> publications;
    private String deliveryMethod;
    private String paymentMethod;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal exciseTax;
    private BigDecimal salesTax;
    private BigDecimal total;
}
