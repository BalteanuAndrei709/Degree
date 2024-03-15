package com.example.degree.Service;

import com.example.degree.Entity.ConsultationDocument;
import com.example.degree.Repository.ConsultationDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultationDocumentService {

    @Autowired
    private ConsultationDocumentRepository consultationDocumentRepository;


    public ConsultationDocument saveConsultationDocument(ConsultationDocument consultationDocument) {
        return consultationDocumentRepository.save(consultationDocument);
    }
}
