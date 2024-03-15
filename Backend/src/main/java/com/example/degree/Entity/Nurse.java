package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "nurse")
@Entity
public class Nurse {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "license_id",unique = true)
    private String licenseId;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "account_id",  referencedColumnName = "id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    //unique = true
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

}
