package com.example.degree.Response;

import com.example.degree.Entity.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicInfoResponse {

    private String name;

    private String surname;

    private Specialization specialization;

    private Integer id;
}
