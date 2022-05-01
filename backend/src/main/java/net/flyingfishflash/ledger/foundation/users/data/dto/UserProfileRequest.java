package net.flyingfishflash.ledger.foundation.users.data.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserProfileRequest(
    @NotBlank @Size(max = 50) @Email String email,
    @NotBlank @Size(max = 50) String firstName,
    @NotBlank @Size(max = 50) String lastName,
    @NotBlank @Size(max = 120) @JsonIgnore String password) {}
