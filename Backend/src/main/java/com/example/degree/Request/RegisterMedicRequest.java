package com.example.degree.Request;

import com.example.degree.Entity.Specialization;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMedicRequest {

    private String socialId;

    private String licenseId;

    private Specialization specialization;

    private String email;

    private String name;

    private String surname;

    private String age;

    private String gender;
}
