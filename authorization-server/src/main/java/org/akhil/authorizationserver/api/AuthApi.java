package org.akhil.authorizationserver.api;

import lombok.RequiredArgsConstructor;
import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.akhil.authorizationserver.dto.MessageDto;
import org.akhil.authorizationserver.service.user.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final UserServiceImpl userService;

    /**
     *
     * @param userDto
     * @return messageDto
     */

    @PostMapping("/create")
    public ResponseEntity<MessageDto> createUser(@RequestBody CreateAppUserDto userDto){

            MessageDto messageDto = userService.createUser(userDto);

            return new ResponseEntity<MessageDto>(messageDto, HttpStatus.CREATED);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateAppUserDto user) {

        return userService.saveUser(user);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }
    @PostMapping("/testMail")
    public ResponseEntity<?> testMail(){
        return userService.sendMailTest();
    }

}
