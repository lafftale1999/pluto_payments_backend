package com.otulp.pluto_payments_backend.DTOs;

import com.otulp.pluto_payments_backend.Models.Invoice;
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
public class SmallInvoiceDTO {
    @NotNull
    @NotEmpty
    private LocalDate invoiceDate;

    @NotNull
    @NotEmpty
    private Invoice.Status status;

    @NotNull
    @NotEmpty
    private float sum;
}
