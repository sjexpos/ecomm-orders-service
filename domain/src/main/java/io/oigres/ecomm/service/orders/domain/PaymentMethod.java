package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import javax.persistence.*;

import io.oigres.ecomm.service.orders.enums.PaymentMethodEnum;

@Entity
@Table(name = "payment_methods")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentMethod extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private PaymentMethodEnum name;
}
