package com.example.degree.Controller;

import com.example.degree.Request.DocumentRequest;
import com.example.degree.Request.RenameDocumentRequest;
import com.example.degree.Response.DocumentInformationResponse;
import com.example.degree.Response.Response;
import com.example.degree.Service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/file")
@RequiredArgsConstructor
public class Document {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadFile(@RequestParam MultipartFile file, HttpServletRequest request){
        return ResponseEntity.ok(documentService.uploadFile(file,request));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Object> downloadFile(@PathVariable Integer fileId, HttpServletRequest request){
        return documentService.downloadFile(fileId,request);
    }

    @GetMapping("medic/downloadFile")
    public ResponseEntity<Object> MedicDownloadPatientFile(@RequestParam Integer documentId, HttpServletRequest request){
        return documentService.downloadPatientFile(documentId,request);
    }

    @GetMapping("/documentsOfPatient")
    public ResponseEntity<List<DocumentInformationResponse>> getAllDocumentOfPatient(@RequestParam  Integer patientId, HttpServletRequest request){
        List<DocumentInformationResponse> documentInformationResponse = documentService.getDocumentsOfPatient(patientId,request);
        if(documentInformationResponse == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(documentInformationResponse);
    }


    @GetMapping("/all")
    public List<DocumentInformationResponse> seeAllFiles(HttpServletRequest request){
        return documentService.getAllFiles(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteFile(@RequestBody DocumentRequest document, HttpServletRequest request){
        List<DocumentInformationResponse> list =  documentService.deleteFile(document.getId(),request);
        return ResponseEntity.ok(Objects.requireNonNullElse(list, "Failed at deleting file!"));
    }

    @PostMapping("/rename")
    public ResponseEntity<Object> renameFile(@RequestBody RenameDocumentRequest document, HttpServletRequest request){
        List<DocumentInformationResponse> list =  documentService.renameFile(document,request);
        return ResponseEntity.ok(Objects.requireNonNullElse(list, "Failed at deleting file!"));
    }






}
