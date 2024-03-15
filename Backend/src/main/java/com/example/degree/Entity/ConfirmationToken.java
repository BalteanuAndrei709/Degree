package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "confirmation tokens")
@Entity
public class ConfirmationToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "account_id",  referencedColumnName = "id")
    private Account account;

    @Column(nullable = false,unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    public ConfirmationToken(Account account, String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.account = account;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
