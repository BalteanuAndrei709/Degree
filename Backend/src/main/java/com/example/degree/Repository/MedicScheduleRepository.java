package com.example.degree.Repository;

import com.example.degree.Entity.MedicSchedule;
import com.example.degree.Entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface MedicScheduleRepository extends JpaRepository<MedicSchedule,Integer> {
    List<MedicSchedule> findAllBySpecializationAndDayBetween(Specialization specialization, Integer from, Integer to);

    List<MedicSchedule> findAllByMedic_IdAndDayBetween(Integer medicId, Integer from, Integer to);

    Boolean existsByMedic_IdAndDayAndStartHour(Integer medicId, Integer startDay, LocalTime startHour);
}
