package com.otulp.pluto_payments_backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Payment {
    @Min(0)
    private float amount;

    @NotEmpty
    @NotNull
    private String cardNumber;

    @NotEmpty
    @Size(min = 64, max = 64, message = "Pin code not correctly formated")
    private String pinCode;

    @NotEmpty
    @NotNull
    private String currency;

    @NotNull
    private LocalDateTime timeStamp;

    @NotEmpty
    @NotNull
    @Size(min = 64, max = 64, message = "Nonce code not correctly formated")
    private String nonce;

    @NotEmpty
    @NotNull
    private String operation;

    @NotEmpty
    @NotNull
    private String deviceMacAddress;

    @Override
    public String toString() {
        return  "Amount: " + getAmount() + "\n" +
                "Currency: " + getCurrency() + "\n" +
                "Card number: " + getCardNumber() + "\n" +
                "Pin code: " + getPinCode() + "\n" +
                "Timestamp: " + getTimeStamp().toString() + "\n" +
                "Nonce: " + getNonce() + "\n" +
                "Operation: " + getOperation() + "\n" +
                "DeviceId: " + getDeviceMacAddress() + "\n";
    }
}
