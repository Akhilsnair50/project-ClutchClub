package org.akhil.userservice.mapper;

import org.akhil.userservice.dto.UserDto;
import org.akhil.userservice.model.AppUser;

import java.util.Optional;

public class UserMapper {
    public UserDto mapToUserDto (AppUser appUser){
        UserDto userDto = new UserDto(
                appUser.getUsername(),
                appUser.getName(),
                appUser.getEmail(),
                appUser.getAuthenticationType(),
                appUser.isExpired(),
                appUser.isLocked(),
                appUser.isCredentialsExpired(),
                appUser.isDisabled()
        );

        return userDto;
    }
}
