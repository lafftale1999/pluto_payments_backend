package com.otulp.pluto_payments_backend.DTOs;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    @NotEmpty
    @NotNull
    private Long transactionId;

    @NotEmpty
    @NotNull
    private String deviceCompanyName;

    @NotEmpty
    @NotNull
    private LocalDate transactionDate;

    @NotEmpty
    @NotNull
    private float transactionCost;


}
