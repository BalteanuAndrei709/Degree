package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "consultation_document_id",  referencedColumnName = "id")
    private ConsultationDocument consultationDocument;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "appointment_id",  referencedColumnName = "id")
    private Appointment appointment;

    private String reason;

    private String symptoms;

    private String recommendations;

    private String initialDiagnosis;
}
