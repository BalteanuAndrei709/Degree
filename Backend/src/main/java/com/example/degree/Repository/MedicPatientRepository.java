package com.example.degree.Repository;

import com.example.degree.Entity.MedicPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicPatientRepository extends JpaRepository<MedicPatient,Integer> {

    Boolean existsByMedic_IdAndPatient_Id(Integer medicId, Integer patientId);

    Boolean existsByPatient_Id(Integer patientId);

    List<MedicPatient> getAllByMedicId(Integer medicId);

    List<MedicPatient> getAllByPatient_Id(Integer patientId);

    MedicPatient getByMedic_IdAndPatient_Id(Integer medicId, Integer patientId);

    void deleteMedicPatientByMedicIdAndPatientId(Integer medicId, Integer patientId);
}
