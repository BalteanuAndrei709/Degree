package com.example.degree.Service;

import com.example.degree.Entity.Appointment;
import com.example.degree.Entity.Specialization;
import com.example.degree.Response.*;
import com.example.degree.Utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CountyService countyService;


    public AgeStats getAgeStats() {
        List<NumberPerAgeStats> list = new ArrayList<>();
        for (int i = 0; i < 110; i = i + 5) {
            list.add(NumberPerAgeStats
                    .builder()
                    .lowerBound(i)
                    .upperBound(i + 4)
                    .number(0)
                    .build());
        }
        List<Appointment> appointmentList = appointmentService.getAll();
        for (Appointment a : appointmentList) {
            for (NumberPerAgeStats n : list) {
                Integer age = Integer.parseInt(AES.decrypt(a.getPatient().getAge()));

                if (n.getLowerBound() <= age && n.getUpperBound() >= age) {
                    n.setNumber(n.getNumber() +1);
                }
            }
        }
        return AgeStats.builder().ageStatList(list).build();
    }

    public SpecializationStats getSpecializationStats() {
        List<NumberPerSpecializationStats> list = new ArrayList<>();
        List<Specialization> specializationList = List.of(Specialization.class.getEnumConstants());
        for(Specialization s : specializationList){
            list.add(NumberPerSpecializationStats
                    .builder()
                            .specialization(s.toString())
                            .number(0)
                    .build());
        }
        List<Appointment> appointmentList = appointmentService.getAll();
        for (Appointment a : appointmentList) {
            for (NumberPerSpecializationStats n : list) {
                if(n.getSpecialization().equals(a.getSpecialization().toString())){
                    n.setNumber(n.getNumber() + 1);
                }
            }
        }
        return SpecializationStats.builder().specializationStatsList(list).build();
    }

    public CountyStats getCountyStats() {
        List<NumberPerCounty> list = new ArrayList<>();
        List<String> countyNames = countyService.getAllCountyNames();
        Collections.sort(countyNames);
        for(String s : countyNames){
            list.add(NumberPerCounty.builder().county(s).number(0).build());
        }

        List<Appointment> appointmentList = appointmentService.getAll();
        for (Appointment a : appointmentList) {
            for (NumberPerCounty n : list) {
                if(n.getCounty().equals(a.getPatient().getCounty().getName())){
                    n.setNumber(n.getNumber() + 1);
                }
            }
        }
        return CountyStats.builder().numberPerCountyList(list).build();

    }

    public GenderStats getGenderStats() {
        List<NumberPerGender> numberPerGenders = new ArrayList<>();

        List<String> genderList = new ArrayList<>();
        genderList.add("male");
        genderList.add("female");

        for(String g : genderList){
            numberPerGenders.add(NumberPerGender.builder().gender(g).number(0).build());
        }

        List<Appointment> appointmentList = appointmentService.getAll();
        for (Appointment a : appointmentList) {
            for (NumberPerGender n : numberPerGenders) {
                if(n.getGender().equals(AES.decrypt(a.getPatient().getGender()))){
                    n.setNumber(n.getNumber() + 1);
                }
            }
        }
        return GenderStats.builder().numberPerGenderList(numberPerGenders).build();



    }
}
