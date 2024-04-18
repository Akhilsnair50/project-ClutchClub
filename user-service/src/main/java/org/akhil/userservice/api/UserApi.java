package org.akhil.userservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.userservice.dto.MessageDto;
import org.akhil.userservice.dto.UserDto;
import org.akhil.userservice.model.AppUser;
import org.akhil.userservice.service.impl.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
@Slf4j

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

    @GetMapping("/me")
    public UserInfoDto getMe(Authentication auth) {
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            final var email = (String) jwtAuth.getTokenAttributes()
                    .getOrDefault(StandardClaimNames.EMAIL, "");
            final var roles = auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            final var exp = Optional.ofNullable(jwtAuth.getTokenAttributes()
                    .get(JwtClaimNames.EXP)).map(expClaim -> {
                if(expClaim instanceof Long lexp) {
                    return lexp;
                }
                if(expClaim instanceof Instant iexp) {
                    return iexp.getEpochSecond();
                }
                if(expClaim instanceof Date dexp) {
                    return dexp.toInstant().getEpochSecond();
                }
                return Long.MAX_VALUE;
            }).orElse(Long.MAX_VALUE);

            log.info("{},{},{},{}",auth.getName(), email, roles, exp);

            return new UserInfoDto(auth.getName(), email, roles, exp);
        }
        return UserInfoDto.ANONYMOUS;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") Integer id ){
        return userService.findUserById(id);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateUserById(@RequestBody UserUpdateDto userDto,@PathVariable("userId") Integer id){
        return userService.updateUserById(userDto,id);
    }

    @PutMapping("/user/block/{userId}")
    public ResponseEntity<?> userBlockedStatus(@RequestBody Boolean status,@PathVariable("userId") Integer id){
        return userService.updateUserEnabledStatus(status,id);
    }


  /*  @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody String name){
        return new ResponseEntity<>("success",HttpStatus.OK);
    }*/






    public record UserUpdateDto(String name,String username,String email, Boolean disabled){}


    /**
     * @param username a unique identifier for the resource owner in the token (sub claim by default)
     * @param email OpenID email claim
     * @param roles Spring authorities resolved for the authentication in the security context
     * @param exp seconds from 1970-01-01T00:00:00Z UTC until the specified UTC date/time when the access token expires
     */
    public static record UserInfoDto(String username, String email, List<String> roles, Long exp) {
        public static final UserInfoDto ANONYMOUS = new UserInfoDto("", "", List.of(), Long.MAX_VALUE);
    }
}