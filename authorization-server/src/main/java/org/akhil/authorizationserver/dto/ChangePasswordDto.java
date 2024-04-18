package org.akhil.authorizationserver.dto;

import lombok.Builder;

@Builder
public record ChangePasswordDto(String password,String repeatPassword) {
}
