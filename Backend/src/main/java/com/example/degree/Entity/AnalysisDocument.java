package com.example.degree.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "analysis_document")
public class AnalysisDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "appointment_id",  referencedColumnName = "id")
    private Appointment appointment;

    @ManyToMany
    private List<Analysis> required;

    private String docName;

    private String docType;

    @Column(columnDefinition = "MEDIUMBLOB  ")
    private byte[] data;

    @Column(name = "completed", columnDefinition = "boolean default false")
    private Boolean completed = false;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "analysis_result_document_id",  referencedColumnName = "id")
    private AnalysisResultDocument analysisResultDocument;

    private LocalTime doneAt;

}
