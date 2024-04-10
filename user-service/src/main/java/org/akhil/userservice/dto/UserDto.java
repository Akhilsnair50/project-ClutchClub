package org.akhil.userservice.dto;

import org.akhil.userservice.enums.AuthenticationType;

import java.io.Serializable;

/**
 * DTO for {USER}
 */
public record UserDto(String username, String name, String email,
                      AuthenticationType authenticationType, boolean expired,
                      boolean locked, boolean credentialsExpired, boolean disabled) implements Serializable {
}