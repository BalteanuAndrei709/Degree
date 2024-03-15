package com.example.degree.Response;

import com.example.degree.Entity.Specialization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Integer id;

    private Integer day;

    private LocalTime startHour;

    private String medicName;

    private String medicSurname;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private String patientName;

    private String patientSurname;

    private Integer patientId;

    private Boolean ensured;

    private String response;

    private Boolean completed;

    private Boolean resultDone;

}
