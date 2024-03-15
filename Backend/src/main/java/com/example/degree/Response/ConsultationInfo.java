package com.example.degree.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationInfo {
    String patientName;

    String patientSurname;
    String patientAge;
    String patientGender;
    String patientSocialId;
    String patientCounty;
    String patientEmail;

    String medicName;

    String medicSurname;

    String medicEmail;

    String specialization;

    LocalTime appointmentStartHour;

    Integer appointmentDay;

    String reason;

    String symptoms;

    String recommendations;

    String initialDiagnosis;


}
