package com.example.degree.Service;

import com.example.degree.Entity.Account;
import com.example.degree.Entity.Medic;
import com.example.degree.Entity.Nurse;
import com.example.degree.Entity.Role;
import com.example.degree.Repository.NurseRepository;
import com.example.degree.Request.RegisterMedicRequest;
import com.example.degree.Response.Response;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private AccountService accountService;

    public Response saveNurse(RegisterMedicRequest request) {
        String response = checkNurseInfo(request);
        System.out.println(response);
        if (!Objects.equals(response, "")) {
            return null;
        }

        Account account = accountService.createAccount(Role.ROLE_NURSE, request.getName(), request.getSurname(), request.getEmail());

        Nurse nurse = Nurse
                .builder()
                .licenseId(AES.encrypt(request.getLicenseId()))
                .specialization(request.getSpecialization())
                .email(AES.encrypt(request.getEmail()))
                .name(AES.encrypt(request.getName()))
                .surname(AES.encrypt(request.getSurname()))
                .age(AES.encrypt(request.getAge()))
                .gender(AES.encrypt(request.getGender()))
                .account(account)
                .build();

        nurse = nurseRepository.save(nurse);
        System.out.println(nurse);
        if(nurseRepository.existsById(nurse.getId())){
            return Response.builder().response("Success").build();
        }
        return null;
    }

    private String checkNurseInfo(RegisterMedicRequest request) {
        String response = Validator.validateMedicInfo(request);
//        if (!isEmailUsed(medic.getEmail())) {
//            response += "Email is taken.\n";
//        }
        if (isLicenceIdTaken(request.getLicenseId())) {
            response += "License id used.\n";
        }
        return response;
    }

    private boolean isLicenceIdTaken(String licenseId) {
        return nurseRepository.existsNurseByLicenseId(licenseId);
    }
}
