package com.otulp.pluto_payments_backend.Controllers;

import com.otulp.pluto_payments_backend.Models.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(
            @RequestBody Payment payment,
            @RequestHeader("Authorization") String signature) {

        if (payment == null) {
            return ResponseEntity.ok("Payment not parsed correctly");
        }
        System.out.println("Hmac: " + signature);
        System.out.println(payment);

        return ResponseEntity.ok("Payment received properly");
    }
}
