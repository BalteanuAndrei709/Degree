package com.example.degree.Service;


import com.example.degree.Entity.*;
import com.example.degree.Repository.MedicScheduleRepository;
import com.example.degree.Request.NormalAccount;
import com.example.degree.Response.AgeStats;
import com.example.degree.Response.NumberPerAgeStats;
import com.example.degree.Response.Response;
import com.example.degree.Response.SpecializationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service
public class AdminService {


    @Autowired
    private MedicService medicService;

    @Autowired
    private MedicScheduleRepository medicScheduleRepository;

    @Autowired
    private CountyService countyService;

    @Autowired
    private PrefixeService prefixeService;

    @Autowired
    private AccountService accountService;


    public Response saveAdmin(NormalAccount request) {
        String response = accountService.createAccount(Role.ROLE_ADMIN, request.getUsername(), request.getPassword(), request.getEmail()).toString();
        return Response.builder().response(response).build();
    }


    public Map<Integer, List<Map<Specialization, List<Integer>>>> createSchedule() {
        Map<Specialization, List<Integer>> medicsIdBySpecialization = getMedicSpecializationMap();
        Set<Specialization> specializationSet = medicsIdBySpecialization.keySet();

        Map<Integer, List<Map<Specialization, List<Integer>>>> calendar = new HashMap<>();

        for (Specialization specialization : specializationSet) {
            Queue<Integer> names = new LinkedList<>(medicsIdBySpecialization.get(specialization));
            for (int day = 0; day < 32; day++) {
                Integer dayMedicId = names.remove();
                medicScheduleRepository.save(
                        MedicSchedule.builder()
                                .medic(medicService.getMedicByMedicId(dayMedicId))
                                .specialization(medicService.getMedicByMedicId(dayMedicId).getSpecialization())
                                .day(day)
                                .startHour(LocalTime.of(8, 00))
                                .endHour(LocalTime.of(17, 00))
                                .build());
                names.add(dayMedicId);
                Integer nightMedicId = names.remove();
                medicScheduleRepository.save(
                        MedicSchedule.builder()
                                .medic(medicService.getMedicByMedicId(nightMedicId))
                                .specialization(medicService.getMedicByMedicId(dayMedicId).getSpecialization())
                                .day(day)
                                .startHour(LocalTime.of(8, 00))
                                .endHour(LocalTime.of(17, 00))
                                .build());
                names.add(nightMedicId);
                Map<Specialization, List<Integer>> tempMap = new HashMap<>();
                List<Integer> tempList = new ArrayList<>();
                tempList.add(dayMedicId);
                tempList.add(nightMedicId);
                tempMap.put(specialization, tempList);
                if (!calendar.containsKey(day)) {
                    List<Map<Specialization, List<Integer>>> listTemp = new ArrayList<>();
                    listTemp.add(tempMap);
                    calendar.put(day, listTemp);
                } else {
                    calendar.get(day).add(tempMap);
                }
            }
        }
        return calendar;

    }

    public List<SpecializationResponse> getAllSpecialization() {
        List<Specialization> list = List.of(Specialization.class.getEnumConstants());
        List<SpecializationResponse> specList = new ArrayList<>();
        for (Specialization specialization : list) {
            specList.add(SpecializationResponse
                    .builder()
                    .name(specialization.name())
                    .id(specialization.ordinal())
                    .build());
        }
        return specList;
    }

    private Map<Specialization, List<Integer>> getMedicSpecializationMap() {
        Map<Specialization, List<Integer>> specializationListMap = new HashMap<>();
        List<Specialization> list = List.of(Specialization.class.getEnumConstants());
        for (Specialization specialization : list) {
            specializationListMap.put(specialization, medicService.getMedicIdsFromSpecialization(specialization));
        }
        return specializationListMap;
    }


    public String addCountyPrefixes() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(new File("prefix.json"));
            for (JsonNode obj : json) {
                String countyName = obj.get("County").asText();
                String prefixName = obj.get("Prefix").asText();
                County county = countyService.saveCounty1(
                        County
                                .builder()
                                .prefixes(new ArrayList<>())
                                .name(countyName)
                                .build())
                        ;
                Prefix prefix = prefixeService.savePrefix(
                        Prefix
                                .builder()
                                .name(prefixName)
                                .build()
                );
                county.getPrefixes().add(prefix);
                countyService.saveCounty(county);
            }
            return "Success";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
