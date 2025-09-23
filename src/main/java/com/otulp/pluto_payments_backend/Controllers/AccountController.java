package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.DTOs.*;
import com.otulp.pluto_payments_backend.Security.SessionChecker;
import com.otulp.pluto_payments_backend.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return accountService.changePassword(passwordChangeDTO);
    }


    @GetMapping("/card/me")
    public ResponseEntity<?> myCard() {
        String email = SessionChecker.getSessionEmail(); // din helper
        if (email == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        return accountService.cardToCardDTO();
    }

    @RequestMapping("/get_invoice/{invoiceId}")
    public InvoiceDTO getInvoiceById(@PathVariable ("invoiceId")Long invoiceId){
        return accountService.invoiceToInvoiceDTO(invoiceId);
    }

    @RequestMapping("/get_invoices")
    public InvoicesDTO getInvoices(){
        return accountService.invoicesToInvoiceDTO();
    }

    @RequestMapping("/get_account_d")
    public DetailedAccountDTO getAccountByEmail(){
        return accountService.detailedAccountToAccountDTO();
    }

}
