package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUserId(Long Id);
    List<Invoice> getInvoicesByUserId(Long Id);
}
