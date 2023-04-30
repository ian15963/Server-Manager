package com.server.application.service;

import com.server.application.model.User;
import com.server.application.model.VerificationToken;
import com.server.application.repo.UserRepository;
import com.server.application.repo.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository repository;

    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken).orElseThrow(() ->
                new IllegalStateException("token not found"));
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(myToken);
    }

    @Transactional
    public String confirmToken(String token) {

        VerificationToken verificationToken = getVerificationToken(token);
        User user = verificationToken.getUser();

        if (verificationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = verificationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        verificationToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(verificationToken);
        user.setEnabled(true);
        repository.save(user);

        return token;
    }

}
