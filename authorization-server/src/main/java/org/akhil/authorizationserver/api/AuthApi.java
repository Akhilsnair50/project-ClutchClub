package org.akhil.authorizationserver.api;

import lombok.RequiredArgsConstructor;
import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.akhil.authorizationserver.dto.MessageDto;
import org.akhil.authorizationserver.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AppUserService appUserService;

    /**
     *
     * @param userDto
     * @return messageDto
     */

    @PostMapping("/create")
    public ResponseEntity<MessageDto> createUser(@RequestBody CreateAppUserDto userDto){

            MessageDto messageDto = appUserService.createUser(userDto);

            return new ResponseEntity<MessageDto>(messageDto, HttpStatus.CREATED);

    }

}
