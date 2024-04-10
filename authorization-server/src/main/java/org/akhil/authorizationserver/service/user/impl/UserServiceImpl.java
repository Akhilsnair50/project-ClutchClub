package org.akhil.authorizationserver.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.akhil.authorizationserver.dto.MessageDto;
import org.akhil.authorizationserver.enums.AuthenticationType;
import org.akhil.authorizationserver.enums.RoleName;
import org.akhil.authorizationserver.model.AppUser;
import org.akhil.authorizationserver.model.ConfirmationToken;
import org.akhil.authorizationserver.model.Role;
import org.akhil.authorizationserver.repository.AppUserRepo;
import org.akhil.authorizationserver.repository.ConfirmationTokenRepository;
import org.akhil.authorizationserver.repository.RoleRepository;
import org.akhil.authorizationserver.service.mail.EmailService;
import org.akhil.authorizationserver.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final ConfirmationTokenRepository confirmationTokenRepository;


    private final AppUserRepo userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public MessageDto createUser(CreateAppUserDto userDto) {
        AppUser appUser = AppUser.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .authenticationType(AuthenticationType.valueOf("LOCAL"))
                .build();
        Set<Role> roles = new HashSet<>();

        log.info("roles : {}", userDto.roles().toString());


        userDto.roles().forEach(r -> {
            Role role = roleRepository.findByRole(RoleName.valueOf(r))
                    .orElseThrow(() -> new RuntimeException("role not found"));

            log.info("The role inside createUserDto is {}", RoleName.valueOf(r));

            roles.add(role);
        });

        appUser.setRoles(roles);
        userRepository.save(appUser);

        log.info("user: {} saved", appUser.getUsername());

        return new MessageDto("user: " + appUser.getUsername() + " saved successfully");
    }

    @Override
    public ResponseEntity<?> saveUser(CreateAppUserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        AppUser user = AppUser.builder()
                .username(userDto.username())
                .email(userDto.email())
                .disabled(true)
                .password(passwordEncoder.encode(userDto.password()))
                .authenticationType(AuthenticationType.valueOf("LOCAL"))
                .build();
        Set<Role> roles = new HashSet<>();

        log.info("roles : {}", userDto.roles().toString());


        userDto.roles().forEach(r -> {
            Role role = roleRepository.findByRole(RoleName.valueOf(r))
                    .orElseThrow(() -> new RuntimeException("role not found"));

            log.info("The role inside createUserDto is {}", RoleName.valueOf(r));

            roles.add(role);
        });

        user.setRoles(roles);

        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("clutchclub@app.com");
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:9000/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            AppUser user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setDisabled(false);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }


    public ResponseEntity<?> sendMailTest(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("akhilsnair500@gmail.com");
        mailMessage.setFrom("clutchclub@app.com");
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:9000/confirm-account?token=" );
        emailService.sendEmail(mailMessage);

        return new ResponseEntity<>(mailMessage, HttpStatus.OK);
    }
}