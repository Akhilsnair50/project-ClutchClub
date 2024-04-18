package org.akhil.authorizationserver.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.authorizationserver.dto.MailBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
@Slf4j
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        System.out.println("----------------------------in EmailService.sendMail()");

        try {
            javaMailSender.send(email);
            log.info("mail successfully sent");
        }catch (Exception e){
            log.error("Failed to send mail",e);
        }
    }

    @Async
    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("overclocked@app.in");
        mailMessage.setTo(mailBody.to());
        mailMessage.setSubject(mailBody.subject());
        mailMessage.setText(mailBody.text());

        javaMailSender.send(mailMessage);
    }

}