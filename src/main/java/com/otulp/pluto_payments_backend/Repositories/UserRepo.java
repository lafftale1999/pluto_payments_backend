package com.otulp.pluto_payments_backend.Repositories;

import com.otulp.pluto_payments_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
