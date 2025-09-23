package com.otulp.pluto_payments_backend.Services;

import com.otulp.pluto_payments_backend.DTOs.*;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Models.Invoice;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    public ResponseEntity<String> changePassword(PasswordChangeDTO passwordChangeDTO);

    public AccountDTO customerToAccountDTO(Customer customer);

    public ResponseEntity<Object> cardToCardDTO(String email);

    public TransactionDTO transactionToCardDTO(TransactionDTO transactionDTO);

    public InvoiceDTO invoiceToInvoiceDTO(Long invoiceid, Long id);

    public InvoicesDTO invoicesToInvoiceDTO(String email);

}
