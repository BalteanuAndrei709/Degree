package com.example.degree.Service;

import com.example.degree.Entity.AnalysisResultDocument;
import com.example.degree.Repository.AnalysisResultDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AnalysisResultDocumentService {

    @Autowired
    private AnalysisResultDocumentRepository analysisResultDocumentRepository;


    public AnalysisResultDocument save(AnalysisResultDocument analysisResultDocument){
       return analysisResultDocumentRepository.save(analysisResultDocument);
    }

}
