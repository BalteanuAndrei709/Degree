package com.example.degree.Response;

import com.example.degree.Entity.Specialization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableMedicResponse {

    private Integer medicId;
    private String medicName;

    private String medicSurname;

    private Integer day;
    @Enumerated(EnumType.STRING)

    private Specialization specialization;

    private List<LocalTime> availableHours;
}
