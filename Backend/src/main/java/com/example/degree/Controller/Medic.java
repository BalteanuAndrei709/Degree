package com.example.degree.Controller;

import com.example.degree.Entity.Specialization;
import com.example.degree.Request.PatientRequest;
import com.example.degree.Response.*;
import com.example.degree.Service.MedicPatientService;
import com.example.degree.Service.MedicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/medic")
public class Medic {

    @Autowired
    private MedicPatientService medicPatientService;

    @Autowired
    private MedicService medicService;

    @GetMapping("/allPatients")
    public ResponseEntity<GetAllPatientResponse> getAllPatients(HttpServletRequest request){
        return ResponseEntity.ok(medicPatientService.getAllPatients(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicBasicInfoResponse>> getAllMedicsName(){
        List<MedicBasicInfoResponse> list = medicService.getAll();
        if(list.size() == 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/allBySpec")
    public ResponseEntity<List<MedicInfoResponse>> getAllMedicsNameBySpecialization(@RequestParam Integer specializationId){
        List<MedicInfoResponse> list = medicService.getAllBySpecialization(specializationId);
        if(list.size() == 0){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/info-medic")
    public ResponseEntity<MedicInfo> getMedicInfo(HttpServletRequest request){
        MedicInfo medicInfo = medicService.getMedicInfo(request);
        if(medicInfo == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(medicInfo);
    }

    @PostMapping("/update-info-medic")
    public ResponseEntity<Boolean> updateMedicInfo(HttpServletRequest request, @RequestBody MedicInfo medicInfo){
        Boolean modified =  medicService.updateDetails(request,medicInfo);
        if(modified == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(modified);
    }
}
