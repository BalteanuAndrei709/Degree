package com.example.degree.Request;

import com.example.degree.Entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Consultation {

    private Patient patient;

    private String reasonAppointment;

    private String preventiveDiagnosis;

    private String recommendations;
}
