package com.example.degree.Service;

import com.example.degree.Repository.AccountRepository;
import com.example.degree.Request.AuthenticateRequest;
import com.example.degree.Response.LoginResponse;
import com.example.degree.Response.Response;
import com.example.degree.Utils.AES;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public LoginResponse authenticate(AuthenticateRequest request) {
        String encryptedUsername = AES.encrypt(request.getUsername());
        if(accountRepository.existsByUsernameAndActivatedIsNull(encryptedUsername)){
            return LoginResponse
                    .builder()
                    .response("Account not activated").build();
        }

         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        encryptedUsername,
                        request.getPassword()
                )
        );

        var user = accountRepository.findByUsername(encryptedUsername).orElseThrow();
        var jwtToken = jwtService.generateToken(user,user.getRole());
        return  LoginResponse.builder()
                .token(jwtToken)
                .response("Success")
                .role(user.getRole().toString())
                .build();
    }
}
