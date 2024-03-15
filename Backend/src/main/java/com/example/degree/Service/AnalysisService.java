package com.example.degree.Service;

import com.example.degree.Entity.Analysis;
import com.example.degree.Repository.AnalysisRepository;
import com.example.degree.Request.AnalysisResponse;
import com.example.degree.Response.AnalysisResponseByCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private AnalysisCategoryService analysisCategoryService;

    public List<AnalysisResponseByCategory> getAllByCategory() {
        List<String> categoryNames = analysisCategoryService.getAllCategoryNames();
        List<AnalysisResponseByCategory> list = new ArrayList<>();
        for (String s: categoryNames ) {
            List<Analysis> analysisList = analysisRepository.getAllByAnalysisCategory_Name(s);
            List<String> analysisNames = this.extractNames(analysisList);
            list.add(AnalysisResponseByCategory
                    .builder()
                    .categoryName(s)
                    .allAnalysisName(analysisNames)
                    .build());
        }
        return list;
    }

    public List<String> extractNames(List<Analysis> list){
        List<String> stringList = new ArrayList<>();
        for (Analysis a: list) {
            stringList.add(a.getName());
        }
        return stringList;
    }


    public Analysis getByName(String name) {
        return analysisRepository.getAnalysisByName(name);
    }

    public AnalysisResponse getAllAnalysisLike(String input) {
        List<Analysis> analysisList = analysisRepository.findAnalysesByNameContaining(input);

        return AnalysisResponse.builder().allAnalysisName(extractNames(analysisList)).build();
    }
}
