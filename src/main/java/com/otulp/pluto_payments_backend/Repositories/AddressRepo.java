package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
