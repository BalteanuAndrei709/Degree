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
@Table(name = "Patients")
@Entity
public class Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "social_id", unique = true)
    private String socialId;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "account_id",  referencedColumnName = "id")
    private Account account;

    @OneToMany
    List<Appointment> appointments;

    @Column(name = "email",unique = true)
    private String email;


    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @ManyToOne
    private County county;

    public Patient(String socialId, String email, String name, String surname, String age, String gender) {
        this.socialId = socialId;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
    }
}
