package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.Card;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Models.TransactionInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TransactionRepo extends JpaRepository<TransactionInformation, Long> {
    List<TransactionInformation> getTransactionsByCard(Card card);
    List<TransactionInformation> getTransactionsByUserId(Long id);
}
