package com.example.degree.Repository;

import com.example.degree.Entity.ConsultationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationDocumentRepository extends JpaRepository<ConsultationDocument,Integer> {
}
