package com.example.degree.Controller;

import com.example.degree.Request.AppointmentRequest;
import com.example.degree.Request.SearchAvailableMedicRequest;
import com.example.degree.Response.*;
import com.example.degree.Service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/appointment")
@RequiredArgsConstructor
public class Appointment {


    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/available")
    public ResponseEntity<List<AvailableMedicResponse>> getAvailableDates(@RequestBody SearchAvailableMedicRequest request) {
        return ResponseEntity.ok(appointmentService.getAvailableDates(request));
    }

    @PostMapping("/make")
    public ResponseEntity<String> makeAppointment(@RequestBody AppointmentRequest appointmentRequest, HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.makeAppointment(appointmentRequest, request));
    }

    @PostMapping("/patient/cancel/{appointmentId}")
    public ResponseEntity<String> cancelPatientAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.cancelPatientAppointment(appointmentId, request));
    }

    @PostMapping("/medic/cancel/{appointmentId}")
    public ResponseEntity<String> cancelMedicAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.cancelMedicAppointment(appointmentId, request));
    }

    @GetMapping("/Medics/future")
    public ResponseEntity<AppointmentsTodayResponse> getMedicsAppointmentsToday(HttpServletRequest request) {
        AppointmentsTodayResponse response = appointmentService.getMedicsAppointmentsFuture(request);
        if (!response.getStatus().equals("Success")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Medics/past")
    public ResponseEntity<AppointmentsTodayResponse> getMedicsAppointmentsPast(HttpServletRequest request) {
        AppointmentsTodayResponse response = appointmentService.getMedicsAppointmentsPast(request);
        if (!response.getStatus().equals("Success")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Patients/future")
    public ResponseEntity<AppointmentsTodayResponse> getPatientsAppointmentsFuture(HttpServletRequest request) {

        AppointmentsTodayResponse response = appointmentService.getFuturePatientsAppointments(request);
        if (!response.getStatus().equals("Success")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Patients/past")
    public ResponseEntity<AppointmentsTodayResponse> getPatientsAppointmentsPast(HttpServletRequest request) {

        AppointmentsTodayResponse response = appointmentService.getPastPatientsAppointments(request);
        if (!response.getStatus().equals("Success")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/start/{appointmentId}")
    public ResponseEntity<Boolean> startAppointment(@PathVariable Integer appointmentId, HttpServletRequest request) {
        Boolean response = appointmentService.startAppointment(appointmentId, request);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPatientId/{appointmentId}")
    public ResponseEntity<Integer> getPatientId(@PathVariable Integer appointmentId, HttpServletRequest request) {
        Integer patientId = appointmentService.getPatientId(appointmentId, request);
        if (patientId == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(patientId);
    }

    @PostMapping("/end/{appointmentId}")
    public ResponseEntity<String> endAppointment(@PathVariable Integer appointmentId,
                                                  @RequestParam MultipartFile file,
                                                  @RequestParam("jsonData") String jsonData) {

        String response = appointmentService.endAppointment(appointmentId, file, jsonData);
        if (response.equals("Success")) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


}
