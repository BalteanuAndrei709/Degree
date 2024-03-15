package com.example.degree.Controller;

import com.example.degree.Request.AuthenticateRequest;
import com.example.degree.Response.LoginResponse;
import com.example.degree.Response.Response;
import com.example.degree.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class Login {

    private final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
