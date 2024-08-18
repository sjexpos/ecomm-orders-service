package io.oigres.ecomm.service.orders.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStatusesResponse implements Serializable {
    private LocalDateTime date;
    private String status;
}
