package com.example.degree.Controller;

import com.example.degree.Response.SpecializationResponse;
import com.example.degree.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/specialization")
@RequiredArgsConstructor
public class Specialization {

    @Autowired
    private final AdminService specializationService;

    @GetMapping("/all")
    public ResponseEntity<List<SpecializationResponse>> getAll(){
        List<SpecializationResponse> list = specializationService.getAllSpecialization();
        if(list.size() == 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(list);
    }
}
