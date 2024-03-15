package com.example.degree.Controller;

import com.example.degree.Response.ConsultationInfo;
import com.example.degree.Response.ConsultationInformationResponse;
import com.example.degree.Response.Response;
import com.example.degree.Service.ConsultationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/consultation")
@RequiredArgsConstructor
public class Consultation {

    @Autowired
    private ConsultationService service;

    @GetMapping("download/{appointmentId}")
    public ResponseEntity<Object> downloadAppointmentFile(@PathVariable Integer appointmentId, HttpServletRequest request){
        return service.downloadFile(appointmentId,request);
    }

    @GetMapping("getName/{appointmentId}")
    public ResponseEntity<Response> getConsultationDocumentName(@PathVariable Integer appointmentId, HttpServletRequest request){
        String name = service.getName(appointmentId,request);
        if(name == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Response.builder().response(name).build());
    }

    @GetMapping("/ofPatient")
    public ResponseEntity<List<ConsultationInformationResponse>> getAllDocumentOfPatient(@RequestParam Integer patientId, HttpServletRequest request){
        List<ConsultationInformationResponse> documentInformationResponse = service.getConsultationsOfPatient(patientId,request);

        return ResponseEntity.ok(documentInformationResponse);
    }

    @GetMapping("download")
    public ResponseEntity<Object> medicDownloadPatientFile(@RequestParam Integer consultationId, HttpServletRequest request){
        return service.downloadPatientFile(consultationId,request);
    }

    @GetMapping("/start/{appointmentId}")
    public ResponseEntity<ConsultationInfo> startConsultation(@PathVariable Integer appointmentId, HttpServletRequest request) {
        ConsultationInfo response = service.startConsultation(appointmentId, request);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("getFilename")
    public ResponseEntity<Response> getFilenameOfConsultation(@RequestParam Integer consultationId, HttpServletRequest request){
        String response = service.getFilename(consultationId,request);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(Response.builder().response(response).build());
    }



}
