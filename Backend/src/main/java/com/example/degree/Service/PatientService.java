package com.example.degree.Service;

import com.example.degree.Entity.*;
import com.example.degree.Repository.MedicPatientRepository;
import com.example.degree.Repository.PatientRepository;
import com.example.degree.Request.RegisterPatientRequest;
import com.example.degree.Response.MedicInfoResponse;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.OcrTools;
import com.example.degree.Utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private CountyService countyService;

    @Autowired
    private  OcrTools ocrTools ;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicPatientRepository  medicPatientRepository;


    public String saveUser(RegisterPatientRequest request) {
        String response = checkUserInfo(request);
        if (!Objects.equals(response, "")) {
            return response;
        }

        Account account = accountService.createAccount(Role.ROLE_PATIENT, request.getName(), request.getSurname(),request.getEmail());


        Patient patient = Patient
                .builder()
                .socialId(AES.encrypt(request.getSocialId()))
                .email(AES.encrypt(request.getEmail()))
                .name(AES.encrypt(request.getName()))
                .county(countyService.getCountyFromName(request.getCounty()))
                .surname(AES.encrypt(request.getSurname()))
                .age(AES.encrypt(request.getAge()))
                .gender(AES.encrypt(request.getGender()))
                .account(account)
                .build();


        patientRepository.save(patient);
        return "Successfully created the account!";

    }

    public String checkUserInfo(RegisterPatientRequest user) {
        String response = Validator.validateUserInfo(user);
//        if (isEmailUsed(AES.encrypt(user.getEmail()))) {
//            response += "Email is taken.\n";
//        }
        if (isSocialIdUsed(AES.encrypt(user.getSocialId()))) {
            response += "Social id used.\n";
        }
        return response;
    }

    public Patient getPatientFromEncryptedSocialId(String socialId){
        return patientRepository.getPatientBySocialId(socialId);
    }

    public Integer getPatientIdFromEncryptedSocialId(String encryptedSocialId){
        return patientRepository.getPatientBySocialId(encryptedSocialId).getId();
    }

    public boolean existsBySocialId(String socialId){
        return patientRepository.existsBySocialId(socialId);
    }

    public boolean existsByAccountId(Integer accountId){
        return patientRepository.existsByAccount_Id(accountId);
    }
    private Boolean isSocialIdUsed(String socialId) {
        return patientRepository.existsBySocialId(socialId);
    }

    private Boolean isEmailUsed(String email) {
        return patientRepository.existsByEmail(email);
    }

    public PatientInfo getInfoFromPicture(MultipartFile file) {
        return ocrTools.extractInfo(file);
    }

    public void savePatientByPatient(Patient patient) {
        patientRepository.save(patient);
    }

    public Patient getPatientFromAccountId(Integer id) {
        return patientRepository.getPatientsByAccount_Id(id);
    }

    public String unroll(Integer medicId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = this.getPatientFromAccountId(account.getId());
        if(!medicPatientRepository.existsByMedic_IdAndPatient_Id(medicId,patient.getId())){
            return "You are not enrolled to this medic!";
        }
        MedicPatient medicPatient = medicPatientRepository.getByMedic_IdAndPatient_Id(medicId,patient.getId());
        medicPatientRepository.delete(medicPatient);
        if(!medicPatientRepository.existsByMedic_IdAndPatient_Id(medicId,patient.getId())){
            return "Success at unrolling!";
        }
        return "Failed at unrolling";
    }

    public List<MedicInfoResponse> getAllPatientMedics(HttpServletRequest request) {
        Integer patientId = this.getPatientFromAccountId(jwtService.getAccountFromRequest(request).getId()).getId();
        List<MedicPatient> medicPatientList = medicPatientRepository.getAllByPatient_Id(patientId);
        List<MedicInfoResponse> medicList = new ArrayList<>();
        for (MedicPatient e:medicPatientList) {
            medicList.add(
                    MedicInfoResponse
                            .builder()
                            .id(e.getMedic().getId())
                            .name(AES.decrypt(e.getMedic().getName()))
                            .surname(AES.decrypt(e.getMedic().getSurname()))
                            .specialization(e.getMedic().getSpecialization())
                            .build());
        }
        return medicList;
    }


    public Patient getById(Integer patientId){
        return patientRepository.getPatientById(patientId);
    }

    public PatientInfo getPatientInfo(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = patientRepository.getPatientsByAccount_Id(account.getId());
        if(patient == null){
            return null;
        }
        return PatientInfo
                .builder()
                .age(AES.decrypt(patient.getAge()))
                .gender(AES.decrypt(patient.getGender()))
                .county(patient.getCounty().getName())
                .surname(AES.decrypt(patient.getSurname()).substring(0,1).toUpperCase() + AES.decrypt(patient.getSurname()).substring(1).toLowerCase())
                .name(AES.decrypt(patient.getName()).substring(0,1) + AES.decrypt(patient.getName()).substring(1).toLowerCase())
                .socialId(AES.decrypt(patient.getSocialId()))
                .email(AES.decrypt(patient.getEmail()))
                .build();

    }

    public Boolean updateDetails(HttpServletRequest request, PatientInfo patientInfo) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = patientRepository.getPatientsByAccount_Id(account.getId());
        System.out.println(patientInfo);
        if(AES.decrypt(patient.getSocialId()).equals(patientInfo.getSocialId())){
            patient.setAge(AES.encrypt(patientInfo.getAge()));
            patient.setGender(AES.encrypt(patientInfo.getGender()));
            patient.setEmail(AES.encrypt(patientInfo.getEmail()));

            patient.setCounty(countyService.getCountyFromName(patientInfo.getCounty()));
            patient.setName(AES.encrypt(patientInfo.getName()));
            patient.setSurname(AES.encrypt(patientInfo.getSurname()));

            patientRepository.save(patient);

            return true;
        }
        return false;
    }
}

