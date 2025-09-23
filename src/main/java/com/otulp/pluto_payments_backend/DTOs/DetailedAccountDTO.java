package com.otulp.pluto_payments_backend.DTOs;

import com.otulp.pluto_payments_backend.Models.Address;
import com.otulp.pluto_payments_backend.Models.Card;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DetailedAccountDTO {
    @NotEmpty
    @NotNull
    private String firstName;

    @NotEmpty
    @NotNull
    private String lastName;

    @NotEmpty
    @NotNull
    private String email;

    @NotEmpty
    @NotNull
    private String phoneNum;

    @NotEmpty
    @NotNull
    private Address address;

    @NotEmpty
    @NotNull
    private float points;

    @NotEmpty
    @NotNull
    private float balance;

    @NotEmpty
    @NotNull
    private float creditUsed;

    @NotEmpty
    @NotNull
    private float creditLimit;

    @NotEmpty
    @NotNull
    private List<SmallInvoiceDTO> invoiceDTOs;

    @NotEmpty
    @NotNull
    private SmallCardDTO card;

    @NotEmpty
    @NotNull
    private List<TransactionDTO> transactions;


}
