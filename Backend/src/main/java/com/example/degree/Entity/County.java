package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "countys")
public class County {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private List<Prefix> prefixes;


}
