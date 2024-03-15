package com.example.degree.Controller;

import com.example.degree.Response.AgeStats;
import com.example.degree.Response.CountyStats;
import com.example.degree.Response.GenderStats;
import com.example.degree.Response.SpecializationStats;
import com.example.degree.Service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/data")
@RequiredArgsConstructor
public class Data {
    @Autowired
    private DataService dataService;


    @GetMapping("/age-stats")
    public ResponseEntity<AgeStats> getAgeStats(){
        AgeStats ageStats = dataService.getAgeStats();
        if(ageStats == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ageStats);
    }

    @GetMapping("/specialization-stats")
    public ResponseEntity<SpecializationStats> getSpecializationStats(){
        SpecializationStats specializationStats = dataService.getSpecializationStats();
        if(specializationStats == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(specializationStats);
    }

    @GetMapping("/county-stats")
    public ResponseEntity<CountyStats> getCountyStats(){
        CountyStats countyStats = dataService.getCountyStats();
        if(countyStats == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(countyStats);
    }

    @GetMapping("/gender-stats")
    public ResponseEntity<GenderStats> getGenderStats(){
        GenderStats genderStats = dataService.getGenderStats();
        if(genderStats == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(genderStats);
    }



}
