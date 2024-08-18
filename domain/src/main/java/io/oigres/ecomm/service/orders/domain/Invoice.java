package io.oigres.ecomm.service.orders.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invoice extends DomainEntity {
    private static final long serialVersionUID = -4164258600038645847L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @Column(name = "date")
    private LocalDateTime date;
}
