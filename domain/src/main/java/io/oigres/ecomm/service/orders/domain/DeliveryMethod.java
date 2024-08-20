package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import jakarta.persistence.*;

import io.oigres.ecomm.service.orders.enums.DeliveryMethodEnum;

@Entity
@Table(name = "delivery_methods")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryMethod extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private DeliveryMethodEnum name;
}
