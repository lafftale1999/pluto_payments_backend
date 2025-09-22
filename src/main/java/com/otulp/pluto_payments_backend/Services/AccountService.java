package com.otulp.pluto_payments_backend.Services;

import com.otulp.pluto_payments_backend.DTOs.AccountDTO;
import com.otulp.pluto_payments_backend.DTOs.InvoiceDTO;
import com.otulp.pluto_payments_backend.DTOs.PasswordChangeDTO;
import com.otulp.pluto_payments_backend.DTOs.TransactionDTO;
import com.otulp.pluto_payments_backend.Models.Customer;
import com.otulp.pluto_payments_backend.Models.Invoice;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<String> changePassword(PasswordChangeDTO passwordChangeDTO);

    public AccountDTO customerToAccountDTO(Customer customer);

    public ResponseEntity<Object> cardToCardDTO(Long card);

    public TransactionDTO transactionToCardDTO(TransactionDTO transactionDTO);

    public InvoiceDTO invoiceToInvoiceDTO(Long invoiceid, Long id);

}
