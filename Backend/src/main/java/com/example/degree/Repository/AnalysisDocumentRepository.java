package com.example.degree.Repository;

import com.example.degree.Entity.AnalysisDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisDocumentRepository extends JpaRepository<AnalysisDocument,Integer> {

    AnalysisDocument getAnalysisDocumentByAppointment_Id(Integer appointmentId);

    List<AnalysisDocument> getAnalysisDocumentsByCompletedIsFalse();

    List<AnalysisDocument> getAnalysisDocumentsByCompletedIsTrueAndAnalysisResultDocument_Completed(Boolean analysisResultDocument_completed);
}
