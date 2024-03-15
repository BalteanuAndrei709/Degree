package com.example.degree.Request;

import com.example.degree.Entity.Specialization;
import com.example.degree.Response.CompleteName;
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
public class AppointmentRequest {

    private Integer day;

    private String startHour;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private Integer medicId;

    private Boolean ensured;
}
