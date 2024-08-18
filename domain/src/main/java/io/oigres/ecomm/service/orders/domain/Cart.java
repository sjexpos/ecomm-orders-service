package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "carts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "date")
    private LocalDateTime date;
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    private Set<Order> orders;
}
