package com.example.degree.Service;

import com.example.degree.Entity.*;
import com.example.degree.Repository.ConsultationRepository;
import com.example.degree.Response.ConsultationInfo;
import com.example.degree.Response.ConsultationInformationResponse;
import com.example.degree.Utils.AES;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicService medicService;



    public ResponseEntity<Object> downloadFile(Integer appointmentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Consultation consultation  = getByAppointmentId(appointmentId);
        Patient patient = consultation.getAppointment().getPatient();
        if(patient.getAccount().getId().equals(account.getId())){
            ConsultationDocument consultationDocument = consultation.getConsultationDocument();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(consultationDocument.getDocType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+consultationDocument.getDocName()+"\"")
                    .body(new ByteArrayResource(consultationDocument.getData()));
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    private Consultation getByAppointmentId(Integer consultationId) {
        return consultationRepository.getConsultationByAppointment_Id(consultationId);
    }

    public Consultation saveConsultation(Consultation consultation){
        return consultationRepository.save(consultation);
    }

    public String getName(Integer appointmentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Consultation consultation = getByAppointmentId(appointmentId);
        Patient patient = consultation.getAppointment().getPatient();
        if(patient.getAccount().getId().equals(account.getId())){
            return consultation.getConsultationDocument().getDocName();
        }
        return null;
    }


    public List<ConsultationInformationResponse> getConsultationsOfPatient(Integer patientId, HttpServletRequest request) {

        List<Consultation> list = consultationRepository.getAllByAppointment_Patient_IdAndAppointment_Completed(patientId,true);
        List<ConsultationInformationResponse> list1 = new ArrayList<>();

        for (Consultation consultation: list) {
            ConsultationDocument consultationDocument = consultation.getConsultationDocument();
            list1.add(ConsultationInformationResponse
                    .builder()
                            .id(consultation.getId())
                            .name(consultationDocument.getDocName())
                            .type(consultationDocument.getDocType())
                            .day(consultation.getAppointment().getDay())
                            .medicName(AES.decrypt(consultation.getAppointment().getMedic().getName()))
                            .medicSurname(AES.decrypt(consultation.getAppointment().getMedic().getSurname()))
                            .specialization(consultation.getAppointment().getSpecialization().toString())
                            .appointmentId(consultation.getAppointment().getId())
                            .resultDone(consultation.getAppointment().getAnalysisDocument().getAnalysisResultDocument().getCompleted())
                    .build());
        }
        return list1;
    }

    public ResponseEntity<Object> downloadPatientFile(Integer consultationId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Consultation consultation = getById(consultationId);
        Appointment appointment = consultation.getAppointment();
        ConsultationDocument consultationDocument = consultation.getConsultationDocument();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(consultationDocument.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+consultationDocument.getDocName()+"\"")
                .body(new ByteArrayResource(consultationDocument.getData()));


    }

    public  Consultation getById(Integer consultationId){
        return consultationRepository.getConsultationById(consultationId);
    }

    public String getFilename(Integer consultationId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Consultation consultation = getById(consultationId);
        Appointment appointment = consultation.getAppointment();
        if(account.getId().equals(appointment.getMedic().getId())){
            return consultation.getConsultationDocument().getDocName();
        }
        return null;

    }

    public void deleteById(Integer consultationId) {
        consultationRepository.deleteById(consultationId);
    }

    public ConsultationInfo startConsultation(Integer appointmentId, HttpServletRequest request) {
        Consultation consultation = consultationRepository.getConsultationByAppointment_Id(appointmentId);
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        Appointment appointment = consultation.getAppointment();
        Patient patient = appointment.getPatient();
        if (appointment.getMedic().getId().equals(medic.getId())) {

            return ConsultationInfo
                    .builder()
                    .patientAge(AES.decrypt(patient.getAge()))
                    .patientCounty(patient.getCounty().getName())
                    .patientGender(AES.decrypt(patient.getGender()))
                    .patientName(AES.decrypt(patient.getName()).substring(0,1).toUpperCase() + AES.decrypt(patient.getName()).substring(1).toLowerCase())
                    .patientSurname(AES.decrypt(patient.getSurname()).substring(0,1).toUpperCase() + AES.decrypt(patient.getSurname()).substring(1).toLowerCase())
                    .patientSocialId(AES.decrypt(patient.getSocialId()))
                    .patientEmail(AES.decrypt(patient.getEmail()))
                    .appointmentDay(appointment.getDay())
                    .appointmentStartHour(appointment.getStartHour())
                    .medicEmail(AES.decrypt(medic.getEmail()))
                    .medicName(AES.decrypt(medic.getName()))
                    .medicSurname(AES.decrypt(medic.getSurname()))
                    .specialization(appointment.getSpecialization().toString())
                    .symptoms(consultation.getSymptoms())
                    .recommendations(consultation.getRecommendations())
                    .initialDiagnosis(consultation.getInitialDiagnosis())
                    .reason(consultation.getReason())
                    .build();

        }
        return null;

    }
}
