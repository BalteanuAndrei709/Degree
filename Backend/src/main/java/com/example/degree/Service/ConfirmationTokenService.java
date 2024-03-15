package com.example.degree.Service;

import com.example.degree.Entity.ConfirmationToken;
import com.example.degree.Repository.ConfirmationTokenRepository;
import com.example.degree.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken createToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public Response confirmToken(String token) {
        ConfirmationToken confirmationToken = this.getToken(token).orElseThrow(() ->
                new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            return Response.builder().response("Email already confirmed!").build();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return Response.builder().response("Confirmation token expired!").build();
        }

        this.setConfirmedAt(token);
        confirmationTokenRepository.activateAccount(token);

        return Response.builder().response("Account confirmed successfully!").build();
    }
}
