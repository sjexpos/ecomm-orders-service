package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderProduct extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(name = "dispensary_product_id")
    private Long dispensaryProductId;
    @Column(name = "units")
    private Integer units;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "transaction_id")
    private Long transactionId;
}
