package io.oigres.ecomm.service.orders.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderByIdResponse implements Serializable {
    private Long id;
    private LocalDateTime orderDate;
    private List<OrderPublicationResponse> items;
    private List<GetStatusesResponse> statuses;
    private Long dispensaryId;
    private Long userId;
    private String deliveryMethod;
    private String paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal exciseTax;
    private BigDecimal salesTax;
    private BigDecimal total;
}
