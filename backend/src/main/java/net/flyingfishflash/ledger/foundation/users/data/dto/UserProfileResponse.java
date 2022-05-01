package net.flyingfishflash.ledger.foundation.users.data.dto;

public record UserProfileResponse(
    Long id, String email, String firstName, String lastName, String password) {}
