package com.otulp.pluto_payments_backend.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {

    @NotNull
    private String cardNum;


    @NotNull
    private LocalDate expiryDate;


    private boolean isActive;

    @NotEmpty
    @NotNull
    private List<TransactionDTO> transactions;


}
