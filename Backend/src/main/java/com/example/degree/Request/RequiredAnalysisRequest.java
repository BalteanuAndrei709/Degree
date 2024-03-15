package com.example.degree.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequiredAnalysisRequest {

    private List<AnalysisRequest> analysisName;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnalysisRequest {
        private String text;
    }
}
