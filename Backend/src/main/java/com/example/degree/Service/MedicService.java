package com.example.degree.Service;

import com.example.degree.Entity.Account;
import com.example.degree.Entity.Medic;
import com.example.degree.Entity.Role;
import com.example.degree.Entity.Specialization;
import com.example.degree.Repository.MedicRepository;
import com.example.degree.Request.RegisterMedicRequest;
import com.example.degree.Response.MedicBasicInfoResponse;
import com.example.degree.Response.MedicInfo;
import com.example.degree.Response.MedicInfoResponse;
import com.example.degree.Response.Response;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MedicService {
    @Autowired
    private MedicRepository medicRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;


    public Response saveMedic(RegisterMedicRequest request) {
        String response = checkMedicInfo(request);
        if (!Objects.equals(response, "")) {
            return Response.builder().response(response).build();
        }

        Account account = accountService.createAccount(Role.ROLE_MEDIC, request.getName(), request.getSurname(), request.getEmail());

        Medic medic = Medic
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

        medic = medicRepository.save(medic);

        if (medicRepository.existsMedicById(medic.getId())) {
            return Response.builder().response("Success").build();
        }
        return null;
    }

    public String checkMedicInfo(RegisterMedicRequest medic) {
        String response = Validator.validateMedicInfo(medic);
//        if (!isEmailUsed(medic.getEmail())) {
//            response += "Email is taken.\n";
//        }
        if (!isLicenceIdTaken(medic.getLicenseId())) {
            response += "License id used.\n";
        }
        return response;
    }

    public Medic getMedicByAccountId(Integer accountId) {
        return medicRepository.getMedicByAccount_Id(accountId);
    }

    public boolean existsByMedicId(Integer medicId) {
        return medicRepository.existsMedicById(medicId);
    }

    public Medic getMedicFromRequest(HttpServletRequest request) {
        return getMedicByAccountId(jwtService.getAccountFromRequest(request).getId());
    }

    public boolean existMedicFromRequest(HttpServletRequest request) {
        return getMedicFromRequest(request) != null;
    }

    public Specialization getSpecializationFromMedicId(Integer medicId) {
        return medicRepository.getMedicById(medicId).getSpecialization();
    }


    public List<MedicBasicInfoResponse> getAll() {

        List<MedicBasicInfoResponse> list = new ArrayList<>();
        for (Medic medic : medicRepository.findAll()) {
            list.add(MedicBasicInfoResponse
                    .builder()
                    .completeName(AES.decrypt(medic.getName()) + " " + AES.decrypt(medic.getSurname()))
                    .id(medic.getId())
                    .build());
        }
        return list;
    }

    public List<MedicInfoResponse> getAllBySpecialization(Integer specializationId) {
        List<MedicInfoResponse> medics = new ArrayList<>();
        Specialization[] values = Specialization.values();
        Specialization specialization = values[specializationId];
        for (Medic medic : medicRepository.findAllBySpecialization(specialization)) {
            medics.add(
                    MedicInfoResponse
                            .builder()
                            .specialization(medic.getSpecialization())
                            .surname(AES.decrypt(medic.getSurname()))
                            .name(AES.decrypt(medic.getName()))
                            .id(medic.getId())
                            .build()
            );
        }
        return medics;
    }

    public List<Integer> getMedicIdsFromSpecialization(Specialization specialization) {
        List<Integer> medicIds = new ArrayList<>();
        List<Medic> medicList = medicRepository.findAllBySpecialization(specialization);
        for (Medic medic : medicList) {
            medicIds.add(medic.getId());
        }
        return medicIds;
    }

    public Medic getMedicByMedicId(Integer medicId) {
        return medicRepository.getMedicById(medicId);
    }

    public void saveMedicByMedic(Medic medic) {
        medicRepository.save(medic);
    }

    private Boolean isEmailUsed(String email) {
        return !medicRepository.existsByEmail(email);
    }

    private Boolean isLicenceIdTaken(String licenseId) {
        return !medicRepository.existsByLicenseId(AES.encrypt(licenseId));
    }


    public MedicInfo getMedicInfo(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicRepository.getMedicByAccount_Id(account.getId());
        if (medic == null) {
            return null;
        }
        return MedicInfo
                .builder()
                .age(AES.decrypt(medic.getAge()))
                .gender(AES.decrypt(medic.getGender()))
                .surname(AES.decrypt(medic.getSurname()).substring(0, 1).toUpperCase() + AES.decrypt(medic.getSurname()).substring(1).toLowerCase())
                .name(AES.decrypt(medic.getName()).charAt(0) + AES.decrypt(medic.getName()).substring(1).toLowerCase())
                .licenseId(AES.decrypt(medic.getLicenseId()))
                .email(AES.decrypt(medic.getEmail()))
                .specialization(medic.getSpecialization().name())
                .build();
    }

    public Boolean updateDetails(HttpServletRequest request, MedicInfo medicInfo) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicRepository.getMedicByAccount_Id(account.getId());

        if (AES.decrypt(medic.getLicenseId()).equals(medicInfo.getLicenseId())) {
            medic.setAge(AES.encrypt(medicInfo.getAge()));
            medic.setGender(AES.encrypt(medicInfo.getGender()));
            medic.setEmail(AES.encrypt(medicInfo.getEmail()));
            medic.setName(AES.encrypt(medicInfo.getName()));
            medic.setSurname(AES.encrypt(medicInfo.getSurname()));

            medicRepository.save(medic);

            return true;
        }
        return false;
    }
}

