package com.example.degree.Service;

import com.example.degree.Entity.Account;
import com.example.degree.Entity.ConfirmationToken;
import com.example.degree.Entity.Role;
import com.example.degree.Entity.Specialization;
import com.example.degree.Response.AccountStats;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.CredentialsGenerator;
import com.example.degree.Repository.AccountRepository;
import com.example.degree.Utils.MailSender;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public AccountService(AccountRepository accountRepository, ConfirmationTokenService confirmationTokenService){
        this.accountRepository = accountRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Account createAccount(Role role, String name, String surname, String email){



            String username = CredentialsGenerator.generateUsername(name,surname);

            while(accountRepository.existsByUsername(username)) {
                username =  CredentialsGenerator.generateUsername(name,surname);
            }
            String password = CredentialsGenerator.generatePassword();

            Account accountCreated = Account.builder()
                    .username(username)
                    .password(password)
                    .role(role)
                    .build();


            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = ConfirmationToken
                    .builder()
                    .account(accountCreated)
                    .createdAt(LocalDateTime.now())
                    .token(token)
                    .expiresAt(LocalDateTime.now().plusMinutes(15))
                    .build();
            confirmationToken = confirmationTokenService.createToken(confirmationToken);

            MailSender.sendMail(email, accountCreated, confirmationToken);

            accountCreated.setPassword(passwordEncoder.encode(password));
            accountCreated.setUsername(AES.encrypt(accountCreated.getUsername()));

            return accountRepository.save(accountCreated);

    }

    public Optional<Account> getByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public Account getByAccountId(Integer accountId){
        return accountRepository.getAccountById(accountId);
    }

    public AccountStats getStats() {
        return AccountStats
                .builder()
                .numAccounts(accountRepository.count())
                .numMedics(accountRepository.countAccountsByRole(Role.ROLE_MEDIC))
                .numPatients(accountRepository.countAccountsByRole(Role.ROLE_PATIENT))
                .build();
    }


}
