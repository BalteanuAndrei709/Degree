package com.example.degree.Repository;

import com.example.degree.Entity.AnalysisCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisCategoryRepository extends JpaRepository<AnalysisCategory, Integer> {


}
