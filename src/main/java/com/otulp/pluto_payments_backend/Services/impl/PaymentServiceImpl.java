package com.otulp.pluto_payments_backend.Services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.hash.Hashing;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.Models.Payment;
import com.otulp.pluto_payments_backend.Repositories.*;
import com.otulp.pluto_payments_backend.Security.HmacChecker;
import com.otulp.pluto_payments_backend.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final AddressRepo addressRepo;
    private final CardRepo cardRepo;
    private final CountryRepo countryRepo;
    private final DeviceRepository deviceRepository;
    private final InvoiceRepo invoiceRepo;
    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<String> authorizePayment(String rawBody, HttpHeaders httpHeaders) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Payment payment = null;

        try {
            payment = mapper.readValue(rawBody, Payment.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Unable to map values");
        }

        Device device = deviceRepository.findByMacAddress(payment.getDeviceMacAddress());

        if (!HmacChecker.checkHmac(payment, device, httpHeaders, rawBody)) {
            return ResponseEntity.badRequest().body("HMAC not matching");
        }

        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(now, payment.getTimeStamp());

        long MAXIMUM_TIME_DIFFERENCE_SECONDS = 300;
        if (diff > MAXIMUM_TIME_DIFFERENCE_SECONDS) {
            // LOGG
            return ResponseEntity.badRequest().body("Time difference not accepted");
        }

        return ResponseEntity.ok("Payment received properly");
    }
}
