package com.example.degree.Service;

import com.example.degree.Entity.*;
import com.example.degree.Repository.AnalysisDocumentRepository;
import com.example.degree.Repository.DocumentRepository;
import com.example.degree.Request.RequiredAnalysisRequest;
import com.example.degree.Response.AnalysisDocumentResponse;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.MailSender;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisDocumentService {
    @Autowired
    private AnalysisDocumentRepository analysisDocumentRepository;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AnalysisResultDocumentService analysisResultDocumentService;

    @Autowired
    private DocumentRepository documentRepository;




    public AnalysisDocument saveAnalysisDocument(AnalysisDocument analysisDocument) {
        return analysisDocumentRepository.save(analysisDocument);
    }

    public String uploadFile(Integer appointmentId, MultipartFile file, RequiredAnalysisRequest analysisRequest, HttpServletRequest request) {
        try {
            Account account = jwtService.getAccountFromRequest(request);
            Medic medic = medicService.getMedicByAccountId(account.getId());

            AnalysisDocument analysisDocument = analysisDocumentRepository.getAnalysisDocumentByAppointment_Id(appointmentId);
            Appointment appointment = analysisDocument.getAppointment();
            if (appointment.getMedic().getId().equals(medic.getId())) {
                analysisDocument.setData(file.getBytes());
                analysisDocument.setDocName(file.getName());
                analysisDocument.setDocType(file.getContentType());
                analysisDocument.setDoneAt(LocalTime.now());

                List<Analysis> analysisList = extractAnalysisFromRequest(analysisRequest);

                analysisDocument.setRequired(analysisList);
                AnalysisResultDocument analysisResultDocument = new AnalysisResultDocument();
                analysisResultDocument = analysisResultDocumentService.save(analysisResultDocument);

                analysisDocument.setAnalysisResultDocument(analysisResultDocument);
                analysisDocumentRepository.save(analysisDocument);


                return "Success";

            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }

    private List<Analysis> extractAnalysisFromRequest(RequiredAnalysisRequest analysisRequest){

        List<Analysis> analysisList = new ArrayList<>();
        for (RequiredAnalysisRequest.AnalysisRequest a: analysisRequest.getAnalysisName()) {
            Analysis analysis = analysisService.getByName(a.getText());
            analysisList.add(analysis);
        }
        return analysisList;
    }

    public List<AnalysisDocumentResponse> getAllNotCompleted(HttpServletRequest request) {
        List<AnalysisDocument> analysisDocumentListNotCompleted = analysisDocumentRepository.getAnalysisDocumentsByCompletedIsFalse();

        List<AnalysisDocumentResponse> analysisDocumentResponses = new ArrayList<>();
        for (AnalysisDocument analysisDocument: analysisDocumentListNotCompleted) {
            analysisDocumentResponses.add(AnalysisDocumentResponse
                    .builder()
                            .analysisRequired(analysisDocument.getRequired())
                            .appointmentId(analysisDocument.getAppointment().getId())
                            .specialization(analysisDocument.getAppointment().getSpecialization().toString())
                            .medicName(AES.decrypt(analysisDocument.getAppointment().getMedic().getName()))
                            .medicSurname(AES.decrypt(analysisDocument.getAppointment().getMedic().getSurname()))
                            .patientName(AES.decrypt(analysisDocument.getAppointment().getPatient().getName()))
                            .patientSurname(AES.decrypt(analysisDocument.getAppointment().getPatient().getSurname()))
                            .completed(analysisDocument.getCompleted())
                            .analysisDocumentId(analysisDocument.getId())

                    .build());
        }
        return analysisDocumentResponses;
    }

    public ResponseEntity<Object> downloadFile(Integer appointmentId, HttpServletRequest request) {
        AnalysisDocument analysisDocument = this.findByIdAppointmentId(appointmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(analysisDocument.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+analysisDocument.getDocName()+"\"")
                .body(new ByteArrayResource(analysisDocument.getData()));
    }

    public AnalysisDocument findByIdAppointmentId(Integer appointmentId){
        return analysisDocumentRepository.getAnalysisDocumentByAppointment_Id(appointmentId);
    }

    public String finish(Integer appointmentId) {
        AnalysisDocument analysisDocument = analysisDocumentRepository.getAnalysisDocumentByAppointment_Id(appointmentId);
        analysisDocument.setCompleted(true);
        analysisDocument = analysisDocumentRepository.save(analysisDocument);

        if(analysisDocument.getCompleted().equals(true)){
            return "Success";
        }
        return null;
    }

    public List<AnalysisDocumentResponse> getAllResultsNotCompleted(HttpServletRequest request) {
        List<AnalysisDocument> analysisDocumentListNotCompleted = analysisDocumentRepository.getAnalysisDocumentsByCompletedIsTrueAndAnalysisResultDocument_Completed(false);

        List<AnalysisDocumentResponse> analysisDocumentResponses = new ArrayList<>();
        for (AnalysisDocument analysisDocument: analysisDocumentListNotCompleted) {
            analysisDocumentResponses.add(AnalysisDocumentResponse
                    .builder()
                    .analysisRequired(analysisDocument.getRequired())
                    .appointmentId(analysisDocument.getAppointment().getId())
                    .specialization(analysisDocument.getAppointment().getSpecialization().toString())
                    .medicName(AES.decrypt(analysisDocument.getAppointment().getMedic().getName()))
                    .medicSurname(AES.decrypt(analysisDocument.getAppointment().getMedic().getSurname()))
                    .patientName(AES.decrypt(analysisDocument.getAppointment().getPatient().getName()))
                    .patientSurname(AES.decrypt(analysisDocument.getAppointment().getPatient().getSurname()))
                    .completed(analysisDocument.getAnalysisResultDocument().getCompleted())
                    .analysisDocumentId(analysisDocument.getId())
                    .day(analysisDocument.getAppointment().getDay())
                    .doneAt(analysisDocument.getDoneAt())

                    .build());
        }
        return analysisDocumentResponses;
    }

    public String uploadResultFile(MultipartFile file, Integer appointmentId) {
        try {
            AnalysisDocument analysisDocument = analysisDocumentRepository.getAnalysisDocumentByAppointment_Id(appointmentId);
            Appointment appointment = analysisDocument.getAppointment();

            Patient patient = appointment.getPatient();

            AnalysisResultDocument analysisResultDocument = analysisDocument.getAnalysisResultDocument();
            analysisResultDocument.setCompleted(true);
            analysisResultDocument.setData(file.getBytes());
            analysisResultDocument.setDocType(file.getContentType());
            analysisResultDocument.setDocName(file.getName());
            analysisResultDocument = analysisResultDocumentService.save(analysisResultDocument);

            analysisDocument.setAnalysisResultDocument(analysisResultDocument);

            analysisDocument = analysisDocumentRepository.save(analysisDocument);


            Boolean sended = MailSender.resultDone(AES.decrypt(patient.getEmail()),appointment);

            if(sended){
                return "Success";
            }
            return null;


        }
        catch (Exception e){
            return  null;
        }



    }

    public ResponseEntity<Object> downloadResultFile(Integer appointmentId, HttpServletRequest request) {

        AnalysisDocument analysisDocument = this.findByIdAppointmentId(appointmentId);
        AnalysisResultDocument analysisResultDocument = analysisDocument.getAnalysisResultDocument();
        if(analysisResultDocument == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(analysisResultDocument.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+analysisResultDocument.getDocName()+"\"")
                .body(new ByteArrayResource(analysisResultDocument.getData()));

    }
}
