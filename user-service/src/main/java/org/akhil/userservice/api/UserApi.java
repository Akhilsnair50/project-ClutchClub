package org.akhil.userservice.api;

import lombok.RequiredArgsConstructor;
import org.akhil.userservice.dto.MessageDto;
import org.akhil.userservice.model.AppUser;
import org.akhil.userservice.service.impl.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class UserApi {

    private final UserServiceImp userService;

    @GetMapping("/user")
    public ResponseEntity<MessageDto> user(Authentication authentication){
        return ResponseEntity.ok(new MessageDto("Hello " + authentication.getName()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MessageDto> admin(Authentication authentication){
        return ResponseEntity.ok(new MessageDto("Hello Mr. " + authentication.getName()));
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<AppUser>> listAll(){
        List<AppUser> userList = userService.listAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}