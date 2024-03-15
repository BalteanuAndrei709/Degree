package com.example.degree.Service;

import com.example.degree.Entity.County;
import com.example.degree.Entity.Prefix;
import com.example.degree.Repository.CountyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountyService {

    @Autowired
    private CountyRepository countyRepository;

    public County getCountyFromName(String name){
        return countyRepository.getCountyByName(name);
    }

    public County getCountyFromPrefix(Prefix prefix){
        return countyRepository.getCountyByPrefixesContaining(prefix);
    }

    public void saveCounty(County county){
         countyRepository.save(county);
    }

    public County saveCounty1(County county){
       return countyRepository.save(county);
    }

    public County getCountyById(Integer countyId){
        return countyRepository.getCountyById(countyId);
    }

    public List<String> getAllCountyNames() {
        List<County> countyList = countyRepository.findAll();

        List<String> countyNames = new ArrayList<>();
        for(County c: countyList){
            countyNames.add(c.getName());
        }
        return countyNames;
    }
}
