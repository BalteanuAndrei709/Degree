package com.example.degree.Controller;

import com.example.degree.Request.RequiredAnalysisRequest;
import com.example.degree.Response.AnalysisDocumentResponse;
import com.example.degree.Response.Response;
import com.example.degree.Service.AnalysisDocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/analysis-document")
@RequiredArgsConstructor
public class AnalysisDocument {

    @Autowired
    private AnalysisDocumentService analysisDocumentService;

    @PostMapping("/upload/{appointmentId}")
    public ResponseEntity<Response> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String analysesRequest,
            HttpServletRequest request, @PathVariable String appointmentId) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            RequiredAnalysisRequest.AnalysisRequest[] requests = mapper.readValue(analysesRequest, RequiredAnalysisRequest.AnalysisRequest[].class);
            List<RequiredAnalysisRequest.AnalysisRequest> requestList = Arrays.asList(requests);
            RequiredAnalysisRequest requiredRequest = RequiredAnalysisRequest.builder()
                    .analysisName(requestList)
                    .build();

            String response = analysisDocumentService.uploadFile(Integer.valueOf(appointmentId), file, requiredRequest, request);

            if (response == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(Response.builder().response(response).build());
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload/results/{appointmentId}")
    public ResponseEntity<Response> uploadResultsFile(@RequestParam("file") MultipartFile file,
            HttpServletRequest request, @PathVariable Integer appointmentId){

        String response = analysisDocumentService.uploadResultFile(file,appointmentId);

        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(Response.builder().response(response).build());

    }

    @GetMapping("/allNotCompleted")
    public ResponseEntity<List<AnalysisDocumentResponse>> getAllNotCompleted(HttpServletRequest request){
        List<AnalysisDocumentResponse> response = analysisDocumentService.getAllNotCompleted(request);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/results/allNotCompleted")
    public ResponseEntity<List<AnalysisDocumentResponse>> getAllResultsNotCompleted(HttpServletRequest request){
        List<AnalysisDocumentResponse> response = analysisDocumentService.getAllResultsNotCompleted(request);
        if(response == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{appointmentId}")
    public ResponseEntity<Object> downloadFile(@PathVariable Integer appointmentId, HttpServletRequest request){
        return analysisDocumentService.downloadFile(appointmentId,request);
    }

    @GetMapping("/download/result/{appointmentId}")
    public ResponseEntity<Object> downloadResultFile(@PathVariable Integer appointmentId, HttpServletRequest request){
        return analysisDocumentService.downloadResultFile(appointmentId,request);
    }

    @PostMapping("complete/{appointmentId}")
    public ResponseEntity<Response> finish(@PathVariable Integer appointmentId){
        String response = analysisDocumentService.finish(appointmentId);
        if(response == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(Response.builder().response(response).build());
    }
}
