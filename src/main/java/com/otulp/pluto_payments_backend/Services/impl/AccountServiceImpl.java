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
import com.otulp.pluto_payments_backend.Security.PlutoHasher;
import com.otulp.pluto_payments_backend.Security.SessionChecker;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
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
    public ResponseEntity<String> changePassword(PasswordChangeDTO dto) {

        Customer customer = customerRepo.findByEmail(dto.getEmail());
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String oldHash = PlutoHasher.hashedString(dto.getOldPassword());
        if (!oldHash.equals(customer.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password incorrect");
        }

        if (dto.getNewPassword().equals(dto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("New password must differ from old password");
        }

        String newHash = PlutoHasher.hashedString(dto.getNewPassword());
        customer.setPassword(newHash);
        customerRepo.save(customer);

        return ResponseEntity.ok("Password changed successfully");
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
    public InvoiceDTO invoiceToInvoiceDTO(Long invoiceId) {
        String email  = SessionChecker.getSessionEmail();
        if(email == null){
            return null;
        }
        Customer user = customerRepo.findByEmail(email);
        if(user == null){
            return null;
        }
        Long id = user.getId();
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
    public InvoicesDTO invoicesToInvoiceDTO() {
        String email  = SessionChecker.getSessionEmail();
        if(email == null){
            return null;
        }
        Customer user = customerRepo.findByEmail(email);
        if(user == null){
            return null;
        }
        Long id = user.getId();
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        List<Invoice> invoice = invoiceRepo.getInvoicesByUserId(id);
        for(Invoice i : invoice){
            invoiceDTOS.add(invoiceToInvoiceDTO(i.getId()));
        }
        AccountDTO accountDTO = customerToAccountDTO(user);
        return InvoicesDTO.builder()
                .invoices(invoiceDTOS)
                .account(accountDTO)
                .build();
    }

    @Override
    public DetailedAccountDTO detailedAccountToAccountDTO() {
        String email  = SessionChecker.getSessionEmail();
        if(email == null){
            return null;
        }
        Customer user = customerRepo.findByEmail(email);
        if(user == null){
            return null;
        }
        Long id = user.getId();
        Card card = user.getCard();
        SmallCardDTO cardDto = new SmallCardDTO(card.getCardNum(), card.getExpiryDate(),card.isActive());
        List<TransactionInformation> transactions = transactionRepo.getTransactionsByUserId(id);
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for(TransactionInformation t : transactions){
            transactionDTOs.add(new TransactionDTO(t.getId(),t.getDevice().getCompanyName(), t.getDate(), t.getCost()));
        }
        List<SmallInvoiceDTO> smallInvoiceDTOS = new ArrayList<>();
        List<Invoice> invoices = invoiceRepo.getInvoicesByUserId(id);
        for(Invoice s : invoices){
            smallInvoiceDTOS.add(new SmallInvoiceDTO(s.getFinalDate(),s.getStatus(),s.getTotalSum()));
        }
        return  DetailedAccountDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNumber())
                .address(user.getAddress())
                .points(user.getPoints())
                .balance(user.getCreditLimit() - user.getCreditUsed())
                .creditUsed(user.getCreditUsed())
                .creditLimit(user.getCreditLimit())
                .card(cardDto)
                .transactions(transactionDTOs)
                .invoiceDTOs(smallInvoiceDTOS)
                .build();
    }

    @Override
    public ResponseEntity<Object> cardToCardDTO() {
        String email  = SessionChecker.getSessionEmail();
        if(email == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No session!");
        }
        Customer user = customerRepo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Card card = user.getCard();
        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
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
