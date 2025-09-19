package com.otulp.pluto_payments_backend.Services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<String> authorizePayment(String rawBody, HttpHeaders httpHeaders);
}
