package com.example.degree.Repository;

import com.example.degree.Entity.Consultation;
import com.example.degree.Entity.ConsultationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation,Integer> {

    Consultation getConsultationByAppointment_Id(Integer appointmentId);

    List<Consultation> getAllByAppointment_Patient_IdAndAppointment_Completed(Integer patientId,Boolean completed);

    Consultation getConsultationById(Integer consultationId);
}
