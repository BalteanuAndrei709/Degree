package com.example.degree.Controller;

import com.example.degree.Request.AnalysisResponse;
import com.example.degree.Response.AnalysisResponseByCategory;
import com.example.degree.Service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/analysis")
@RequiredArgsConstructor
public class Analysis {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/all")
    public ResponseEntity<List<AnalysisResponseByCategory>> getAllAnalysis() {
        List<AnalysisResponseByCategory> list = analysisService.getAllByCategory();
        if(list == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/search/{input}")
    public ResponseEntity<AnalysisResponse> getAllAnalysisLike(@PathVariable String input) {

        AnalysisResponse response = analysisService.getAllAnalysisLike(input);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
}
