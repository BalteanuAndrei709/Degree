package com.example.degree.Repository;


import com.example.degree.Entity.Medic;
import com.example.degree.Entity.Specialization;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicRepository extends JpaRepository<Medic,Integer> {

    Boolean existsByLicenseId(String licenseId);
    Boolean existsByEmail(String email);
    Medic getMedicByAccount_Id(Integer accountId);

    Medic getMedicById(Integer medicId);

    Boolean existsMedicById(Integer medicId);

    List<Medic> findAllBySpecialization(Specialization specialization);

}
