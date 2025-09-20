package com.otulp.pluto_payments_backend.Services.impl;

import com.otulp.pluto_payments_backend.DTOs.AccountDTO;
import com.otulp.pluto_payments_backend.DTOs.PasswordChangeDTO;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Repositories.CustomerRepo;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerRepo customerRepo;

    @Override
    public ResponseEntity<String> changePassword(PasswordChangeDTO passwordChangeDTO) {
        passwordChangeDTO.setNewPassword(passwordChangeDTO.getNewPassword());
        Customer customer = customerRepo.findByEmail(passwordChangeDTO.getEmail());
        if(customer != null){
            if(customer.getPassword().equals(passwordChangeDTO.getOldPassword())
                    && !passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getOldPassword())){
                customer.setPassword(passwordChangeDTO.getNewPassword());
                customerRepo.save(customer);
                return ResponseEntity.ok("Password changed successfully");
            }
        }
        return ResponseEntity.badRequest().body("Password change failed.");
    }


    @Override
    public AccountDTO customerToAccountDTO(Customer customer) {
        return AccountDTO.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNum(customer.getPhoneNumber()).build();
    }

}
