package org.akhil.authorizationserver.api;

import lombok.RequiredArgsConstructor;
import org.akhil.authorizationserver.dto.ChangePasswordDto;
import org.akhil.authorizationserver.dto.MailBody;
import org.akhil.authorizationserver.model.AppUser;
import org.akhil.authorizationserver.model.ForgotPassword;
import org.akhil.authorizationserver.repository.AppUserRepo;
import org.akhil.authorizationserver.repository.ForgotPasswordRepository;
import org.akhil.authorizationserver.service.mail.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordApi {

    private final AppUserRepo userRepo;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String > verifyEmail(@PathVariable String email){
        AppUser user = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("couldnt find user with email: "+email));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for password reset request :"+otp)
                .subject("Otp for password reset")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+120*1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent successfully for  "+email);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity <String > verifyOtp(@PathVariable Integer otp,@PathVariable String email){

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("couldnt find user with email: "+email));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(()-> new RuntimeException("Otp is Invalid !!") );

        if (fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("Otp has expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String > changePassword(@RequestBody ChangePasswordDto changePasswordDto, @PathVariable String email){
        if (!Objects.equals(changePasswordDto.password(),changePasswordDto.repeatPassword())){
            return new ResponseEntity<>("please enter the passwords again !!",HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword =passwordEncoder.encode(changePasswordDto.password());
        userRepo.updatePassword(email,encodedPassword);

        return ResponseEntity.ok("password has been changed !");

    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }

}
