package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.DTOs.AccountDTO;
import com.otulp.pluto_payments_backend.DTOs.PasswordChangeDTO;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Repositories.CustomerRepo;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class AccountController {

    private final AccountService accountService;
    private final CustomerRepo customerRepo;

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return accountService.changePassword(passwordChangeDTO);
    }

    @RequestMapping("/get_all/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }

        return accountService.customerToAccountDTO(customer);
    }

}
