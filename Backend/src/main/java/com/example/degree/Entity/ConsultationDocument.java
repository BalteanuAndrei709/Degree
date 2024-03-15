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
@Table(name = "consultation_document")
public class ConsultationDocument {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String docName;

    private String docType;

    @Column(columnDefinition = "MEDIUMBLOB  ")
    private byte[] data;





}
