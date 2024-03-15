package com.example.degree.Repository;

import com.example.degree.Entity.County;
import com.example.degree.Entity.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountyRepository extends JpaRepository<County,Integer> {

    County getCountyByName(String name);

    County getCountyByPrefixesContaining(Prefix prefix);

    County getCountyById(Integer countyId);


}
