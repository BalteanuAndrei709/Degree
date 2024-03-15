package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medics_patients")
@Entity
public class MedicPatient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Medic medic;

    @ManyToOne
    private Patient patient;


    public MedicPatient(Medic medic, Patient patient) {
        this.medic = medic;
        this.patient = patient;
    }
}
