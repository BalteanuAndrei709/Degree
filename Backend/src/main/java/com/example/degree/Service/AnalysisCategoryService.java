package com.example.degree.Service;

import com.example.degree.Entity.AnalysisCategory;
import com.example.degree.Repository.AnalysisCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisCategoryService {

    @Autowired
    private AnalysisCategoryRepository analysisCategoryRepository;

    public List<AnalysisCategory> getAll() {
        return analysisCategoryRepository.findAll();
    }

    public List<String> getAllCategoryNames() {
        List<AnalysisCategory> list = this.getAll();
        List<String> names = new ArrayList<>();
        for (AnalysisCategory x : list) {
            names.add(x.getName());
        }
        return names;
    }
}
