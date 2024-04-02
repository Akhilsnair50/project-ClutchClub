package org.akhil.authorizationserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.akhil.authorizationserver.dto.MessageDto;
import org.akhil.authorizationserver.enums.RoleName;
import org.akhil.authorizationserver.model.AppUser;
import org.akhil.authorizationserver.model.Role;
import org.akhil.authorizationserver.repository.AppUserRepo;
import org.akhil.authorizationserver.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final AppUserRepo userRepo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MessageDto createUser(CreateAppUserDto userDto){
        AppUser appUser = AppUser.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .build();
        Set<Role> roles = new HashSet<>();

        log.info("roles : {}" , userDto.roles().toString());


        userDto.roles().forEach(r -> {
            Role role = roleRepository.findByRole(RoleName.valueOf(r))
                    .orElseThrow(()->new RuntimeException("role not found"));

            log.info("The role inside createUserDto is {}" , RoleName.valueOf(r));

            roles.add(role);
        });

        appUser.setRoles(roles);
        userRepo.save(appUser);

        log.info("user: {} saved",appUser.getUsername());

        return new MessageDto("user: "+ appUser.getUsername()+" saved successfully");
    }
}
