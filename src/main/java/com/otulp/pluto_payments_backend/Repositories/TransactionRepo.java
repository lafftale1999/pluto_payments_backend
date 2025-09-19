package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.TransactionInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionInformation, Long> {
}
