package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.DTOs.*;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return accountService.changePassword(passwordChangeDTO);
    }


    @RequestMapping("/get_card/{email}")
    public ResponseEntity<Object> getCardsById(@PathVariable String email){
        return accountService.cardToCardDTO(email);
    }

    @RequestMapping("/get_invoice/{invoiceId}/{userId}")
    public InvoiceDTO getInvoiceById(@PathVariable ("invoiceId")Long invoiceId, @PathVariable("userId") Long userId){
        return accountService.invoiceToInvoiceDTO(invoiceId, userId);
    }

    @RequestMapping("/get_invoices/{email}")
    public InvoicesDTO getInvoices(@PathVariable String email){
        return accountService.invoicesToInvoiceDTO(email);
    }

    @RequestMapping("/get_account_d/{email}")
    public DetailedAccountDTO getAccountByEmail(@PathVariable String email){
        return accountService.detailedAccountToAccountDTO(email);
    }

}
