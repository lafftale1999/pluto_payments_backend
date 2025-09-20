package com.otulp.pluto_payments_backend.Services;

import com.otulp.pluto_payments_backend.DTOs.AccountDTO;
import com.otulp.pluto_payments_backend.DTOs.PasswordChangeDTO;
import com.otulp.pluto_payments_backend.Models.Customer;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<String> changePassword(PasswordChangeDTO passwordChangeDTO);

    public AccountDTO customerToAccountDTO(Customer customer);

}
