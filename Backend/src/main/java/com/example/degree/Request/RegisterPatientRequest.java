package com.example.degree.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequest {

    private String socialId;
    private String email;

    private String county;

    private String name;

    private String surname;

    private String age;

    private String gender;

}
