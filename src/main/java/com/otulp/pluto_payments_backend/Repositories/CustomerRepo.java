package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.Card;
import com.otulp.pluto_payments_backend.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Customer findByCard(Card card);
    Customer findByEmail(String email);
}
