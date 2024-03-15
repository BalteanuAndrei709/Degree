package com.example.degree.Service;

import com.example.degree.Entity.*;
import com.example.degree.Repository.DocumentRepository;
import com.example.degree.Request.RenameDocumentRequest;
import com.example.degree.Response.DocumentInformationResponse;
import com.example.degree.Response.Response;
import jakarta.servlet.http.HttpServletRequest;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicPatientService medicPatientService;


    public void saveDocument(Document document){
        documentRepository.save(document);
    }

    public Response uploadFile(MultipartFile document, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);

        //check if file is already uploaded
        if(documentRepository.existsByAccount_IdAndDocName(account.getId(),document.getOriginalFilename()) == Boolean.TRUE){
            return Response.builder()
                    .response("File already uploaded!")
                    .build();
        }

        // Save the file to the database
        try {
            Document newDocument = Document
                    .builder()
                    .docName(document.getOriginalFilename())
                    .docType(document.getContentType())
                    .data(document.getBytes())
                    .account(account)
                    .build();

            documentRepository.save(newDocument); // Assuming a file repository class that interacts with the database

            return Response.builder()
                    .response("File uploaded successfully!")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.builder()
                    .response("Error at uploading document!")
                    .build();
        }

    }

    public ResponseEntity<Object> downloadFile(Integer fileId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);

        if(documentRepository.existsByAccount_IdAndId(account.getId(),fileId) == Boolean.FALSE){

            return ResponseEntity.ok(Response.builder().response("File not found or inaccessible!").build());
        }

        Document doc = documentRepository.findById(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
                .body(new ByteArrayResource(doc.getData()));
    }

    public List<DocumentInformationResponse> getAllFiles(HttpServletRequest request) {
        List<Document> documentList =  documentRepository.getAllByAccount_Id(jwtService.getAccountFromRequest(request).getId());
        return getListInformationFromListDocument(documentList);
    }

    public List<DocumentInformationResponse> getAllFilesByAccountId(Integer accountId){
        return getListInformationFromListDocument(documentRepository.getAllByAccount_Id(accountId));
    }
    public List<DocumentInformationResponse> deleteFile(Integer documentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);


        if(!documentRepository.existsByAccount_IdAndId(account.getId(),documentId)){
            return null;
        }

        Document document = documentRepository.getById(documentId);

        document.setAccount(null);
        documentRepository.save(document);

        documentRepository.deleteById(document.getId());

        List<Document> documentList =  documentRepository.getAllByAccount_Id(jwtService.getAccountFromRequest(request).getId());

        return getListInformationFromListDocument(documentList);

    }

    private List<DocumentInformationResponse> getListInformationFromListDocument(List<Document> documentList){
        List<DocumentInformationResponse> documentInformationList = new ArrayList<>();

        for (Document doc:
                documentList) {
            documentInformationList.add(
                    DocumentInformationResponse
                            .builder()
                            .id(doc.getId())
                            .name(doc.getDocName())
                            .type(doc.getDocType())
                            .build());
        }
        return documentInformationList;
    }

    public List<DocumentInformationResponse> renameFile(RenameDocumentRequest documentRequest, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);


        if(!documentRepository.existsByAccount_IdAndId(account.getId(),documentRequest.getDocumentId())){
            return null;
        }
        Document document = documentRepository.getById(documentRequest.getDocumentId());
        document.setDocName(documentRequest.getNewFilename());

        documentRepository.save(document);

        List<Document> documentList =  documentRepository.getAllByAccount_Id(account.getId());

        return getListInformationFromListDocument(documentList);


    }

    public Document getById(Integer documentId){
        return documentRepository.getById(documentId);
    }

    public ResponseEntity<Object> downloadPatientFile(Integer documentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Document document = getById(documentId);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        Patient patient = patientService.getPatientFromAccountId(document.getAccount().getId());
        if(medicPatientService.isPatientEnrolledToMedic(medic.getId(),patient.getId())){
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getDocType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+document.getDocName()+"\"")
                    .body(new ByteArrayResource(document.getData()));
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public List<DocumentInformationResponse> getDocumentsOfPatient(Integer patientId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());

        Patient patient = patientService.getById(patientId);
        if(medicPatientService.isPatientEnrolledToMedic(medic.getId(),patient.getId())){
            return getAllFilesByAccountId(patient.getAccount().getId());
        }
        return null;

    }
}
