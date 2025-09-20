package com.otulp.pluto_payments_backend.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainDTO {

    @NotEmpty
    @NotNull
    private float credit;

    @NotEmpty
    @NotNull
    private float balance;

    @NotEmpty
    @NotNull
    private float points;

    @NotEmpty
    @NotNull
    private String cardNumber;

    @NotEmpty
    @NotNull
    private List<InvoiceDTO> invoices;

    @NotEmpty
    @NotNull
    private AccountDTO account;

}
