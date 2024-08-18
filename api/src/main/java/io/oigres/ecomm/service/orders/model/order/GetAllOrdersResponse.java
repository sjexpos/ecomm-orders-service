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
public class GetAllOrdersResponse implements Serializable {
    private Long id;
    private LocalDateTime orderDate;
    private String orderStatus;
    private LocalDateTime orderStatusDate;
    private List<OrderPublicationResponse> items;
    private Long userId;
    private Long dispensaryId;
    private BigDecimal total;
}
