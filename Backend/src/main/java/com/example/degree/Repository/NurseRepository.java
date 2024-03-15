package com.example.degree.Repository;

import com.example.degree.Entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse,Integer> {

    Boolean existsNurseByLicenseId(String licenseId);
}
