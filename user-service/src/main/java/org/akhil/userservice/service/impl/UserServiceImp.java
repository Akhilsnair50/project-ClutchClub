package org.akhil.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.userservice.enums.RoleName;
import org.akhil.userservice.model.AppUser;
import org.akhil.userservice.model.Role;
import org.akhil.userservice.repository.UserRepository;
import org.akhil.userservice.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;


    @Override
    public List<AppUser> listAll() {
        List<AppUser> userList =  userRepository.findAll();

        log.info("{}",userList);
        List<AppUser> filteredList = userList.stream()
                .filter(u -> u.getRoles().stream()
                        .noneMatch(r -> r.getRole() == RoleName.ROLE_ADMIN))
                .collect(Collectors.toList());



        log.info("filtered list : {}",filteredList);
        return filteredList;
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<AppUser> disableByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void deleteUserByUsername(String username) {

    }
}
