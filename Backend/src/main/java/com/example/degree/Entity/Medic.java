package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medics")
@Entity
public class Medic {

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

    @OneToMany
    List<Appointment> appointments;

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

    public Medic(String licenseId, String email, String name, String surname, String age, String gender) {
        this.licenseId = licenseId;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
    }
}
