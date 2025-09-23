package com.otulp.pluto_payments_backend.DTOs;

import com.otulp.pluto_payments_backend.Models.Invoice;
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
public class InvoicesDTO {
    @NotNull
    @NotEmpty
    private List<InvoiceDTO> invoices;

    @NotNull
    @NotEmpty
    private AccountDTO account;

}
