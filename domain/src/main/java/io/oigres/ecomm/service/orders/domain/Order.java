package io.oigres.ecomm.service.orders.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.oigres.ecomm.service.orders.enums.OrderStatusEnum;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Cache(region = "ordersCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "dispensary_id")
    private Long dispensaryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    @Column(name = "date")
    private LocalDateTime date;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
    @Column(name = "subtotal_price")
    private BigDecimal subtotalPrice;
    @Column(name = "excise_tax")
    private BigDecimal exciseTax;
    @Column(name = "sales_tax")
    private BigDecimal salesTax;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<Message> messages;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderStatus> orderStatuses;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderProduct> orderProducts;
    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private OrderStatusEnum lastStatus;
}
