package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<Country, Long> {
}
