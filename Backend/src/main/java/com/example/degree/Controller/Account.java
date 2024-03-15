package com.example.degree.Controller;

import com.example.degree.Response.AccountStats;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class Account {

    @Autowired
    private AccountService accountService;


    @CrossOrigin
    @GetMapping("/stats")
    public ResponseEntity<AccountStats> getStats() {
        return ResponseEntity.ok(accountService.getStats());
    }



}

