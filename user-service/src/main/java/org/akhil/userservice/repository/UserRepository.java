package org.akhil.userservice.repository;

import org.akhil.userservice.enums.AuthenticationType;
import org.akhil.userservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser,Integer> {
    public Optional<AppUser> findByEmail(String email);
    public Optional<AppUser> findByUsername(String username);
    public Optional<AppUser> findByAuthenticationType(AuthenticationType authenticationType);

}
