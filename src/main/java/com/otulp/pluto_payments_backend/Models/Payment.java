package com.otulp.pluto_payments_backend.Models;

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

    @Size(min = 64, max = 64, message = "Signature code not correctly formated")
    private String signature;

    @NotEmpty
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

    @NotEmpty
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
    private String deviceId;

    public Payment(String signature, float amount, String cardNumber, String pinCode, String currency,
                   String timeStamp, String nonce, String operation, String deviceId) {
        setSignature(signature);
        setAmount(amount);
        setCardNumber(cardNumber);
        setPinCode(pinCode);
        setCurrency(currency);

        LocalDateTime dateTime = LocalDateTime.parse(timeStamp);
        setTimeStamp(dateTime);

        setNonce(nonce);
        setOperation(operation);
        setDeviceId(deviceId);
    }

    @Override
    public String toString() {
        return "Signature: " + getSignature() + "\n" +
                "Amount: " + getAmount() + "\n" +
                "Currency: " + getCurrency() + "\n" +
                "Card number: " + getCardNumber() + "\n" +
                "Pin code: " + getPinCode() + "\n" +
                "Timestamp: " + getTimeStamp().toString() + "\n" +
                "Nonce: " + getNonce() + "\n" +
                "Operation: " + getOperation() + "\n" +
                "DeviceId: " + getDeviceId() + "\n";
    }
}
