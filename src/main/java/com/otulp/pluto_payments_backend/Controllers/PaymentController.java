package com.otulp.pluto_payments_backend.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.Models.Payment;
import com.otulp.pluto_payments_backend.Repositories.DeviceRepository;
import com.otulp.pluto_payments_backend.Security.HmacChecker;
import com.otulp.pluto_payments_backend.Services.PaymentService;
import com.otulp.pluto_payments_backend.Services.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final DeviceRepository deviceRepository;

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(
            @RequestBody String rawBody,
            @RequestHeader HttpHeaders headers) {

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Payment payment = null;

        try {
            payment = mapper.readValue(rawBody, Payment.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Unable to map values");
        }

        Device device = deviceRepository.findByMacAddress(payment.getDeviceMacAddress());

        if (!HmacChecker.checkHmac(payment, device, headers, rawBody)) {
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
