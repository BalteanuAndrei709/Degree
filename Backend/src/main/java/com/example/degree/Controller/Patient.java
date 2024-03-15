package com.example.degree.Controller;

import com.example.degree.Response.MedicInfoResponse;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patient")
public class Patient {

    @Autowired
    private PatientService patientService;

    @GetMapping("/myMedics")
    public ResponseEntity<List<MedicInfoResponse>> allPatientMedics(HttpServletRequest request){
        List<MedicInfoResponse> list = patientService.getAllPatientMedics(request);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/unroll/{medicId}")
    public ResponseEntity<String> unrollPatient(HttpServletRequest request, @PathVariable Integer medicId){
        return ResponseEntity.ok(patientService.unroll(medicId,request));
    }

    @GetMapping("/info-patient")
    public ResponseEntity<PatientInfo> getPatientInfo(HttpServletRequest request){
        PatientInfo patientInfo = patientService.getPatientInfo(request);
        if(patientInfo == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(patientInfo);
    }

    @PostMapping("/update-info-patient")
    public ResponseEntity<Boolean> getPatientInfo(HttpServletRequest request, @RequestBody PatientInfo patientInfo){
        Boolean modified = patientService.updateDetails(request,patientInfo);
        if(modified == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(modified);
    }



}
