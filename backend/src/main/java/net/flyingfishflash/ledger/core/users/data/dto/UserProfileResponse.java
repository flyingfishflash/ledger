package net.flyingfishflash.ledger.core.users.data.dto;

public record UserProfileResponse(
    Long id, String email, String firstName, String lastName, String password) {}
