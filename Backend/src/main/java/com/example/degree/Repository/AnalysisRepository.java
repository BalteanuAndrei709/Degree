package com.example.degree.Repository;

import com.example.degree.Entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis,Integer> {

    List<Analysis> getAllByAnalysisCategory_Name(String categoryName);

    Analysis getAnalysisByName(String name);

    List<Analysis> findAnalysesByNameContaining(String input);
}
