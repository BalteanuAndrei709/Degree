package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "analysis_result_documents")
@Entity
public class AnalysisResultDocument {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String docName;

    private String docType;

    @Column(columnDefinition = "MEDIUMBLOB  ")
    private byte[] data;

    @Column(name = "completed", columnDefinition = "boolean default false")
    private Boolean completed = false;
}
