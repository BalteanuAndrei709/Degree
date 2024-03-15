package com.example.degree.Controller;

import com.example.degree.Request.NormalAccount;
import com.example.degree.Request.RegisterMedicRequest;
import com.example.degree.Request.RegisterPatientRequest;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Response.Response;
import com.example.degree.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/registration")
public class Registration {


@Autowired
    private PatientService patientService;

    @Autowired
    private ConfirmationTokenService tokenService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private NurseService nurseService;


    @PostMapping("/patient")
    public ResponseEntity<String> addPatient(@RequestBody RegisterPatientRequest request) {
        System.out.println(request);
        String response = patientService.saveUser(request);
        if(response.equals("Successfully created the account!")){
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/medic")
    public ResponseEntity<Response>  addMedic(@RequestBody RegisterMedicRequest request) {
        Response response = medicService.saveMedic(request);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/nurse")
    public ResponseEntity<Response>  addNurse(@RequestBody RegisterMedicRequest request) {
        System.out.println(request);
        Response response = nurseService.saveNurse(request);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<Response>  addMedic(@RequestBody NormalAccount request) {
        return ResponseEntity.ok(adminService.saveAdmin(request));
    }


    @GetMapping(path = "confirm")
    public ResponseEntity<Response> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(tokenService.confirmToken(token));
    }

    @PostMapping("/analyze-photo")
    public ResponseEntity<PatientInfo> getInfoFromPicture(@RequestParam MultipartFile file){
        return ResponseEntity.ok(patientService.getInfoFromPicture(file));
    }
}
