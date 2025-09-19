package com.otulp.pluto_payments_backend.Services;

import com.otulp.pluto_payments_backend.Models.Payment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface PaymentService {
    ResponseEntity<String> authorizePayment(String rawBody, HttpHeaders httpHeaders);
}
