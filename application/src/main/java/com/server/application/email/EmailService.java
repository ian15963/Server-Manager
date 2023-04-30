package com.server.application.email;

import com.server.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String send(User user, String subject, String text){

        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(emailSender);
            email.setTo(user.getEmail());
            email.setSubject(subject);
            email.setText(text);
            mailSender.send(email);
            return "Email enviado com sucesso";
        }catch (Exception e){
            return "Erro ao enviar o email";
        }

    }

}
