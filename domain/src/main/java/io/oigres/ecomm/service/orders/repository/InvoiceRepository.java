package io.oigres.ecomm.service.orders.repository;

import java.util.Optional;

import io.oigres.ecomm.service.orders.domain.Invoice;

public interface InvoiceRepository extends GenericRepository<Invoice, Long> {
    Optional<Invoice> findById(long id);
}
