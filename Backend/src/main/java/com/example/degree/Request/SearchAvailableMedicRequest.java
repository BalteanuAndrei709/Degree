package com.example.degree.Request;

import com.example.degree.Entity.Specialization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAvailableMedicRequest {

    private Integer dayFrom;
    private Integer dayTo;
    private Integer specializationId;

    private Integer medicId;

}
