package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(
            @RequestBody String rawBody,
            @RequestHeader HttpHeaders headers) {
        System.out.println("============================== BEGINNING OF REQUEST ========================");
        return paymentService.authorizePayment(rawBody, headers);
    }
}
