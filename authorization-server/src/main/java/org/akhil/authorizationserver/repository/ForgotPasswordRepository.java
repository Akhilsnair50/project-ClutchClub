package org.akhil.authorizationserver.repository;

import org.akhil.authorizationserver.model.AppUser;
import org.akhil.authorizationserver.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and  fp.user= ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, AppUser user);
}