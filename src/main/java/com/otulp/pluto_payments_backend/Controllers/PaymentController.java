package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(
            @RequestBody String rawBody,
            @RequestHeader HttpHeaders headers) {

        return paymentService.authorizePayment(rawBody, headers);
    }
}
