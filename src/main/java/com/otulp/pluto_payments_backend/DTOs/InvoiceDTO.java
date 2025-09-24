package com.otulp.pluto_payments_backend.DTOs;

import com.otulp.pluto_payments_backend.Models.Invoice;
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
public class InvoiceDTO {

    @NotNull
    @NotEmpty
    private Long id;

    @NotNull
    @NotEmpty
    private LocalDate invoiceDate;

    @NotNull
    @NotEmpty
    private Invoice.Status status;

    @NotNull
    @NotEmpty
    private List<TransactionDTO> transactions;

    @NotNull
    @NotEmpty
    private float sum;
}
