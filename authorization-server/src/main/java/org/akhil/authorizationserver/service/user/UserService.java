package org.akhil.authorizationserver.service.user;

import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> saveUser(CreateAppUserDto user);
    ResponseEntity<?> confirmEmail(String confirmationToken);
}
