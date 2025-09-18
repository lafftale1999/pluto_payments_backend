package com.otulp.pluto_payments_backend.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.hash.Hashing;
import com.otulp.pluto_payments_backend.Models.Device;
import com.otulp.pluto_payments_backend.Models.Payment;
import com.otulp.pluto_payments_backend.PlutoHelpers.PlutoFormater;
import com.otulp.pluto_payments_backend.Repositories.DeviceRepository;
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
public class PaymentController {

    private final long MAXIMUM_TIME_DIFFERENCE_SECONDS = 300;
    private final DeviceRepository deviceRepository;

    public PaymentController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> authorizePayment(
            @RequestBody String rawBody,
            @RequestHeader HttpHeaders headers) {

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Payment payment = null;

        try {
            payment = mapper.readValue(rawBody, Payment.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Not accepted");
        }

        Device device = deviceRepository.findByMacAddress(payment.getDeviceMacAddress());

        if (!PlutoFormater.check_hmac(payment, device, headers, rawBody)) {
            return ResponseEntity.badRequest().body("Not accepted");
        }

        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(now, payment.getTimeStamp());

        if (diff > MAXIMUM_TIME_DIFFERENCE_SECONDS) {
            // LOGG
            return ResponseEntity.badRequest().body("Not accepted");
        }

        return ResponseEntity.ok("Payment received properly");
    }
}
