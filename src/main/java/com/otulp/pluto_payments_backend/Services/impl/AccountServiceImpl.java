package com.otulp.pluto_payments_backend.Services.impl;

import com.otulp.pluto_payments_backend.DTOs.*;
import com.otulp.pluto_payments_backend.Models.Card;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Models.Invoice;
import com.otulp.pluto_payments_backend.Models.TransactionInformation;
import com.otulp.pluto_payments_backend.Repositories.CardRepo;
import com.otulp.pluto_payments_backend.Repositories.CustomerRepo;
import com.otulp.pluto_payments_backend.Repositories.InvoiceRepo;
import com.otulp.pluto_payments_backend.Repositories.TransactionRepo;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerRepo customerRepo;
    private final TransactionRepo transactionRepo;
    private final CardRepo cardRepo;
    private final InvoiceRepo invoiceRepo;

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
                .phoneNum(customer.getPhoneNumber())
                .build();
    }

    @Override
    public TransactionDTO transactionToCardDTO(TransactionDTO transactionDTO) {
        return TransactionDTO.builder()
                .transactionId(transactionDTO.getTransactionId())
                .transactionCost(transactionDTO.getTransactionCost())
                .deviceCompanyName(transactionDTO.getDeviceCompanyName())
                .transactionDate(transactionDTO.getTransactionDate())
                .build();
    }

    @Override
    public InvoiceDTO invoiceToInvoiceDTO(Long invoiceId, Long id) {
        List<TransactionInformation> transactions = transactionRepo.getTransactionsByUserId(id);
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        Invoice invoice = invoiceRepo.getReferenceById(invoiceId);
        for(TransactionInformation t : transactions){
            transactionDTOs.add(new TransactionDTO(t.getId(),t.getDevice().getCompanyName(), t.getDate(), t.getCost()));
        }
        return InvoiceDTO.builder()
                .invoiceDate(invoice.getFinalDate())
                .status(invoice.getStatus())
                .transactions(transactionDTOs)
                .sum(invoice.getTotalSum())
                .build();
    }

    @Override
    public InvoicesDTO invoicesToInvoiceDTO(String email) {
        Customer user = customerRepo.findByEmail(email);
        Long id = user.getId();
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        List<Invoice> invoice = invoiceRepo.getInvoicesByUserId(id);
        for(Invoice i : invoice){
            invoiceDTOS.add(invoiceToInvoiceDTO(i.getId(),id));
        }
        AccountDTO accountDTO = customerToAccountDTO(user);
        return InvoicesDTO.builder()
                .invoices(invoiceDTOS)
                .account(accountDTO)
                .build();
    }


    @Override
    public ResponseEntity<Object> cardToCardDTO(String email) {
        Customer user =  customerRepo.findByEmail(email);
        Long id = user.getId();
        Card card = cardRepo.findById(id).orElse(null);
        if(card == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<TransactionInformation> transactions = transactionRepo.getTransactionsByCard(card);
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for(TransactionInformation t : transactions){
            transactionDTOs.add(new TransactionDTO(t.getId(),t.getDevice().getCompanyName(), t.getDate(), t.getCost()));
        }
        CardDTO returnCard = CardDTO.builder()
                .cardNum(card.getCardNum())
                .expiryDate(card.getExpiryDate())
                .isActive(card.isActive())
                .transactions(transactionDTOs)
                .build();
        return ResponseEntity.ok(returnCard);
    }




}
