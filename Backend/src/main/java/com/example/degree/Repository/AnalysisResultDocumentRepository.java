package com.example.degree.Repository;

import com.example.degree.Entity.AnalysisResultDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnalysisResultDocumentRepository extends JpaRepository<AnalysisResultDocument,Integer> {
}
