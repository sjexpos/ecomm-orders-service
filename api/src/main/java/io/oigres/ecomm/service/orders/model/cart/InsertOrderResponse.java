package io.oigres.ecomm.service.orders.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertOrderResponse {
    private Long orderId;
    private List<InsertOrderDispensaryPublicationResponse> publications;
    private Long dispensaryId;
    private String deliveryMethod;
    private String paymentMethod;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal exciseTax;
    private BigDecimal salesTax;
    private BigDecimal total;

}
