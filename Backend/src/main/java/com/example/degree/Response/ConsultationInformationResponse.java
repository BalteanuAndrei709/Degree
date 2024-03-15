package com.example.degree.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationInformationResponse {

    private Integer id;

    private String name;


    private String type;

    private String medicName;

    private String medicSurname;

    private String specialization;

    private Integer day;

    private Integer appointmentId;

    private Boolean resultDone;
}
