package org.akhil.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.userservice.api.UserApi;
import org.akhil.userservice.dto.UserDto;
import org.akhil.userservice.enums.RoleName;
import org.akhil.userservice.mapper.UserMapper;
import org.akhil.userservice.model.AppUser;
import org.akhil.userservice.model.Role;
import org.akhil.userservice.repository.UserRepository;
import org.akhil.userservice.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<AppUser> userList = userRepository.findAll();

        log.info("{}", userList);
        List<AppUser> filteredList = userList.stream()
                .filter(u -> u.getRoles().stream()
                        .noneMatch(r -> r.getRole() == RoleName.ROLE_ADMIN))
                .collect(Collectors.toList());


        log.info("filtered list : {}", filteredList);
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

    public ResponseEntity<UserDto> findUserById(Integer id) {

        Optional<AppUser> user = userRepository.findById(id);
        UserMapper userMapper = new UserMapper();
        AppUser appUser = user.get();
        UserDto userDto = userMapper.mapToUserDto(appUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    public ResponseEntity<?> updateUserById(UserApi.UserUpdateDto userDto, Integer id){
        Optional<AppUser> optionalUser = userRepository.findById(id);

        AppUser user= optionalUser.get();
        user.setEmail(userDto.email());
        user.setUsername(userDto.username());
        user.setDisabled(userDto.disabled());

        try {
            userRepository.save(user);
            return new ResponseEntity<>(userDto,HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("An error occured while updating the user");
        }
    }

    public ResponseEntity<?> updateUserEnabledStatus(Boolean status,Integer id){
        Optional<AppUser> optionalUser = userRepository.findById(id);

        AppUser user= optionalUser.get();

        user.setLocked(!user.isLocked());

        try {
            userRepository.save(user);
            return new ResponseEntity<>(user.isEnabled(),HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("An error occured while updating the blocked status of the user");
        }

    }


}
