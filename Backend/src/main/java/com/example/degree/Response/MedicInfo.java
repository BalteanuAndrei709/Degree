package com.example.degree.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicInfo {

    private String name;

    private String surname;

    private String specialization;

    private String licenseId;


    private String email;

    private String age;

    private String gender;


}

