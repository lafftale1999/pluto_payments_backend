package com.otulp.pluto_payments_backend.Services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otulp.pluto_payments_backend.DTOs.PaymentDTO;
import com.otulp.pluto_payments_backend.Models.*;
import com.otulp.pluto_payments_backend.Repositories.*;
import com.otulp.pluto_payments_backend.Security.HmacChecker;
import com.otulp.pluto_payments_backend.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final CustomerRepo customerRepo;

    private boolean isWithinTimeFrame(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(now, timestamp);
        long MAXIMUM_TIME_DIFFERENCE_SECONDS = 300;

        return diff < MAXIMUM_TIME_DIFFERENCE_SECONDS;
    }

    private boolean isExpired(Card card) {
        return LocalDate.now().isAfter(card.getExpiryDate());
    }

    @Override
    public ResponseEntity<String> authorizePayment(String rawBody, HttpHeaders httpHeaders) {

        System.out.println(rawBody);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        PaymentDTO payment = null;

        try {
            payment = mapper.readValue(rawBody, PaymentDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Unknown error");
        }

        // CHECK DEVICE
        Device device = deviceRepository.findByMacAddress(payment.getDeviceMacAddress());
        if (device == null || !device.isApproved()) {
            return ResponseEntity.badRequest().body("Not authorized");
        }

        // CHECK INTEGRITY
        if (!HmacChecker.checkHmac(device, httpHeaders, rawBody)) {
            return ResponseEntity.badRequest().body("Not authorized");
        }

        // CHECK REPLAY
        if (!isWithinTimeFrame(payment.getTimeStamp())) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        // CHECK CARD
        Card card = cardRepo.findByCardNum(payment.getCardNumber());
        if (card == null) {
            return ResponseEntity.badRequest().body("Not authorized");
        }
        if (!card.isActive()) {
            return ResponseEntity.badRequest().body("Card inactive\nContact supplier");
        }
        if (isExpired(card)) {
            card.setActive(false);
            cardRepo.save(card);
            return ResponseEntity.badRequest().body("Card expired\nContact supplier");
        }
        if (!card.checkPinCode(payment.getPinCode())) {
            card.wrongCodeEntered();
            cardRepo.save(card);
            if (!card.isActive()) {
                return ResponseEntity.badRequest().body("Card inactivated\nContact supplier");
            }
            return ResponseEntity.badRequest().body("Wrong code\nTries left: " + (3 - card.getWrongEntries()));
        }

        card.setWrongEntries(0);
        cardRepo.save(card);

        Customer customer = customerRepo.findByCard(card);
        if (payment.getAmount() > customer.getAvailableCredit()) {
            return ResponseEntity.badRequest().body("Denied\nBalance too low");
        }

        transactionRepo.save(new TransactionInformation(
                null,
                payment.getAmount(),
                payment.getTimeStamp().toLocalDate(),
                device,
                customer,
                card
        ));

        customer.updateCredit(payment.getAmount());
        customerRepo.save(customer);

        return ResponseEntity.ok("Payment Accepted");
    }
}
