package com.example.degree.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NumberPerAgeStats {

    private Integer lowerBound;

    private Integer upperBound;
    private Integer number;
}
