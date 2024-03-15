package com.example.degree.Controller;

import com.example.degree.Response.Response;
import com.example.degree.Service.MedicPatientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/appointment")
@RequiredArgsConstructor
public class MedicPatient {

    @Autowired
    private MedicPatientService medicPatientService;

    @GetMapping("/enrollPatient")
    public ResponseEntity<Response> enrollPatient(@RequestParam Integer patientId, HttpServletRequest request){
        String response = medicPatientService.enrollPatient(patientId,request);
        if(response != null){
            return ResponseEntity.ok(Response.builder().response(response).build());
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
