package com.example.degree.Service;


import com.example.degree.Entity.*;
import com.example.degree.Repository.MedicPatientRepository;
import com.example.degree.Response.*;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MedicPatientService {


    @Autowired
    private MedicPatientRepository medicPatientRepository;


    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicService medicService;


    public String enrollPatient(Integer patientId, HttpServletRequest request) {

        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        Patient patient = patientService.getById(patientId);
        if (!medicPatientRepository.existsByMedic_IdAndPatient_Id(medic.getId(), patient.getId())) {
            medicPatientRepository.save(MedicPatient
                    .builder()
                    .patient(patient)
                    .medic(medic)
                    .build());
        }
        if (medicPatientRepository.existsByMedic_IdAndPatient_Id(medic.getId(), patient.getId())) {
            return "Success";
        }
        return "Error";
    }

    public GetAllPatientResponse getAllPatients(HttpServletRequest request) {

        if (!medicService.existMedicFromRequest(request)) {
            return GetAllPatientResponse
                    .builder()
                    .response("No medic with this id!")
                    .build();
        }

        Medic medic = medicService.getMedicFromRequest(request);
        List<MedicPatient> medicPatientList = medicPatientRepository.getAllByMedicId(medic.getId());
        List<PatientInfo> patientList = new ArrayList<>();
        for (MedicPatient medicPatient : medicPatientList) {
            patientList.add(PatientInfo
                    .builder()
                    .patientId(medicPatient.getPatient().getId())
                    .socialId(AES.decrypt(medicPatient.getPatient().getSocialId()))
                    .patientId(medicPatient.getPatient().getId())
                    .name(AES.decrypt(medicPatient.getPatient().getName()))
                    .surname(AES.decrypt(medicPatient.getPatient().getSurname()))
                    .county(medicPatient.getPatient().getCounty().getName())
                    .gender(AES.decrypt(medicPatient.getPatient().getGender()))
                    .age(AES.decrypt(medicPatient.getPatient().getAge()))
                    .build());
        }
        return GetAllPatientResponse
                .builder()
                .response("Success")
                .patientList(patientList)
                .build();

    }

    public Boolean isPatientEnrolledToMedic(Integer medicID, Integer patientId) {
        return medicPatientRepository.existsByMedic_IdAndPatient_Id(medicID, patientId);
    }

    private String isDataValidForEnrolling(String patientSocialId, Integer medicId) {

        if (!Validator.isSocialIdValid(patientSocialId)) {
            return "Social Id is not correct!";
        }

        String encryptedSocialId = AES.encrypt(patientSocialId);
        if (!patientService.existsBySocialId(encryptedSocialId)) {
            return "Patient not found";
        }


        if (medicService.existsByMedicId(medicId)) {
            return "Only medics can enroll patients";
        }


        if (isPatientEnrolledToMedic(medicId, patientService.getPatientIdFromEncryptedSocialId(encryptedSocialId))) {
            return "Patient already enrolled!";
        }


        if (isPatientAlreadyEnrolledSpecialization(medicService.getSpecializationFromMedicId(medicId), patientService.getPatientIdFromEncryptedSocialId(encryptedSocialId))) {
            return "Patient already enrolled at a different medic with same specialization! ";
        }
        return null;
    }

    private String isDataValidForGettingPatientDocuments(String patientSocialId, Integer medicId) {
        if (!Validator.isSocialIdValid(patientSocialId)) {
            return "Social Id Invalid";
        }
        if (!medicPatientRepository.existsByMedic_IdAndPatient_Id(medicId, patientService.getPatientIdFromEncryptedSocialId(AES.encrypt(patientSocialId)))) {
            return "Patient not enrolled for this medic!";
        }
        return null;
    }

    private Boolean isPatientAlreadyEnrolledSpecialization(Specialization specialization, Integer patientId) {
        List<MedicPatient> medicPatientList = medicPatientRepository.getAllByPatient_Id(patientId);

        for (MedicPatient x : medicPatientList) {
            if (x.getMedic().getSpecialization().equals(specialization)) {
                return true;
            }
        }
        return false;
    }

    private String isDataValidForUnrolling(Integer patientId, Integer medicId) {

        if (medicService.existsByMedicId(medicId)) {
            return "Only medics can enroll patients";
        }

        if (!isPatientEnrolledToMedic(medicId, patientId)) {
            return "No patient enrolled with this social ID at this medic!";
        }

        return null;
    }

    public List<MedicPatient> allPatientsMedics(Integer patientId) {
        return medicPatientRepository.getAllByPatient_Id(patientId);
    }

}
