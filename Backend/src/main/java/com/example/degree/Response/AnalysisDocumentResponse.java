package com.example.degree.Response;

import com.example.degree.Entity.Analysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDocumentResponse {

    private Integer appointmentId;

    private Integer analysisDocumentId;

    private String patientName;

    private String patientSurname;

    private String specialization;

    private String medicName;

    private String medicSurname;

    private List<Analysis> analysisRequired;

    private Boolean completed;

    private Integer day;

    private LocalTime doneAt;


}
