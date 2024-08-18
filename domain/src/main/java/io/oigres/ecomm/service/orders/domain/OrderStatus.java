package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_statuses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatus extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
    @Column(name = "date")
    private LocalDateTime date;
}
