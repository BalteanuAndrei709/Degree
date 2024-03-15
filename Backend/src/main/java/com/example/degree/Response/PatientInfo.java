package com.example.degree.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientInfo {

    Integer patientId;
    String name;

    String surname;
    String age;

    String gender;

    String socialId;

    String county;

    String email;
}
