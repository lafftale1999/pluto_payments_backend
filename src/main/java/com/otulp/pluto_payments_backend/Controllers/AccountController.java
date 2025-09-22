package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.DTOs.InvoiceDTO;
import com.otulp.pluto_payments_backend.DTOs.InvoicesDTO;
import com.otulp.pluto_payments_backend.DTOs.PasswordChangeDTO;
import com.otulp.pluto_payments_backend.DTOs.TransactionDTO;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return accountService.changePassword(passwordChangeDTO);
    }


    @RequestMapping("/get_card/{id}")
    public ResponseEntity<Object> getCardsById(@PathVariable Long id){
        return accountService.cardToCardDTO(id);
    }

    @RequestMapping("/get_invoice/{invoiceId}/{userId}")
    public InvoiceDTO getInvoiceById(@PathVariable ("invoiceId")Long invoiceId, @PathVariable("userId") Long userId){
        return accountService.invoiceToInvoiceDTO(invoiceId, userId);
    }

    @RequestMapping("/get_invoices/{id}")
    public InvoicesDTO getInvoices(@PathVariable Long id){
        return accountService.invoicesToInvoiceDTO(id);
    }

}
