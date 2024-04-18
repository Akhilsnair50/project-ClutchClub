package org.akhil.cloudgateway.api;

import org.akhil.cloudgateway.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/me")
public class LoginHelperApi {
//    @GetMapping()
//    public ResponseEntity<UserDto> loggedInUser(Authentication auth){
//        UserDto userDto = new UserDto(user.getName());
//        return new ResponseEntity<>(userDto,HttpStatus.OK);
//    }
}
