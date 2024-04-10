package org.akhil.userservice.service;

import org.akhil.userservice.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public List<AppUser> listAll();

    public Optional<AppUser> findByUsername(String username);


    public Optional<AppUser> findByEmail(String email);
    public Optional<AppUser> disableByEmail(String email);

    public void deleteUserByUsername(String username);
}
