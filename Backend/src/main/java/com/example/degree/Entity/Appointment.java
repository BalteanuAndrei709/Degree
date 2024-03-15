package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer day;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Medic medic;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private LocalTime startHour;

    private Boolean ensured;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "consultation_id",  referencedColumnName = "id")
    private Consultation consultation;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "analysis_document_id",  referencedColumnName = "id")
    private AnalysisDocument analysisDocument;

    @Column(name = "completed", columnDefinition = "boolean default false")
    private Boolean completed = false;
}
