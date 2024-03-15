package com.example.degree.Repository;

import com.example.degree.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Boolean existsByEmail(String email);

    Boolean existsBySocialId(String socialId);

    Boolean existsByAccount_Id(Integer accountId);

    Patient getPatientBySocialId(String socialId);

    Patient getPatientsByAccount_Id(Integer accountId);

    Patient getPatientById(Integer patientId);
}
