package com.otulp.pluto_payments_backend.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeDTO {

    @NotEmpty
    @NotNull
    private String email;
    @NotEmpty
    @NotNull
    private String oldPassword;
    @NotEmpty
    @NotNull
    private String newPassword;

}
