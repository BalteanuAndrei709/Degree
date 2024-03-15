package com.example.degree.Repository;

import com.example.degree.Entity.Appointment;
import com.example.degree.Entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> getAllByMedicIdAndDay(Integer medicId, Integer day);

    List<Appointment> getAllByMedicIdAndDayIsGreaterThanEqualAndCompletedIsFalseAndStartHourIsAfter(Integer medicId, Integer day, LocalTime localTime);

    List<Appointment> getAllByMedicIdAndDayIsLessThanEqualAndCompletedIsTrue(Integer medicId, Integer day);

    List<Appointment> getAllByPatient_IdAndDayGreaterThan(Integer patientId, Integer day);

    List<Appointment> getAllByPatient_IdAndDayIsAndStartHourGreaterThanEqual(Integer patientId, Integer day, LocalTime time);


    List<Appointment> getAllByPatient_IdAndDayLessThanOrCompletedIsTrue(Integer patientId, Integer day);

    Boolean existsByMedic_IdAndSpecializationAndDayAndStartHour(Integer medicId, Specialization specialization, Integer day, LocalTime startHour);

    Appointment getAppointmentById(Integer appointmentId);

    boolean existsAppointmentById(Integer appointmentId);


}


