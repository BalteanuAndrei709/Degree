package com.example.degree.Service;

import com.example.degree.Entity.*;
import com.example.degree.Repository.AppointmentRepository;
import com.example.degree.Repository.MedicScheduleRepository;
import com.example.degree.Request.AppointmentRequest;
import com.example.degree.Request.SearchAvailableMedicRequest;
import com.example.degree.Response.AppointmentResponse;
import com.example.degree.Response.AppointmentsTodayResponse;
import com.example.degree.Response.AvailableMedicResponse;
import com.example.degree.Utils.AES;
import com.example.degree.Utils.MailSender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class AppointmentService {
    @Autowired
    private MedicScheduleRepository medicScheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicService medicService;


    @Autowired
    private MedicPatientService medicPatientService;

    @Autowired
    private ConsultationDocumentService consultationDocumentService;

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private AnalysisDocumentService analysisDocumentService;

    public List<AvailableMedicResponse> getAvailableDates(SearchAvailableMedicRequest request) {
        if (request.getMedicId() == null) {
            return getSpecializationAvailableSpots(request);
        } else {
            return getMedicAvailableSpots(request);
        }
    }

    private List<AvailableMedicResponse> getMedicAvailableSpots(SearchAvailableMedicRequest request) {
        List<MedicSchedule> medicSchedulesList = medicScheduleRepository.findAllByMedic_IdAndDayBetween(request.getMedicId(), request.getDayFrom(), request.getDayTo());
        return calculateAvailableSpots(medicSchedulesList);
    }

    private List<AvailableMedicResponse> getSpecializationAvailableSpots(SearchAvailableMedicRequest request) {
        Specialization[] values = Specialization.values();
        Specialization specialization = values[request.getSpecializationId()];
        List<MedicSchedule> medicSchedulesList = medicScheduleRepository.findAllBySpecializationAndDayBetween(specialization, request.getDayFrom(), request.getDayTo());
        return calculateAvailableSpots(medicSchedulesList);
    }


    private List<AvailableMedicResponse> calculateAvailableSpots(List<MedicSchedule> medicSchedulesList) {

        List<AvailableMedicResponse> availableSpotsList = new ArrayList<>();
        for (MedicSchedule medicSchedule : medicSchedulesList) {

            List<Appointment> appointmentsMedicToday = appointmentRepository.getAllByMedicIdAndDay(medicSchedule.getMedic().getId(), medicSchedule.getDay());
            List<LocalTime> startingHoursToday = new ArrayList<>();
            for (Appointment appointment : appointmentsMedicToday) {
                startingHoursToday.add(appointment.getStartHour());
            }

            LocalDate today = LocalDate.now();
            int dayOfMonth = today.getDayOfMonth();

            if (medicSchedule.getDay().equals(dayOfMonth)) {
                int hours = 8;
                int minutes = 0;
                while (LocalTime.of(hours, minutes).isBefore(LocalTime.now())) {

                    startingHoursToday.add(LocalTime.of(hours, minutes));
                    if (minutes == 0) {
                        minutes += 30;
                    } else if (minutes == 30) {
                        minutes = 0;
                        hours += 1;
                    }
                }
            }

            int hours = 8;
            int minutes = 0;
            List<LocalTime> timesAvailable = new ArrayList<>();
            while (hours < 17) {
                if (!startingHoursToday.contains(LocalTime.of(hours, minutes))) {
                    timesAvailable.add(LocalTime.of(hours, minutes));
                }
                if (minutes == 0) {
                    minutes += 30;
                } else if (minutes == 30) {
                    minutes = 0;
                    hours += 1;
                }
            }
            if(timesAvailable.size() > 0){
                availableSpotsList.add(AvailableMedicResponse
                        .builder()
                        .day(medicSchedule.getDay())
                        .availableHours(timesAvailable)
                        .medicId(medicSchedule.getMedic().getId())
                        .medicName(AES.decrypt(medicSchedule.getMedic().getName()))
                        .medicSurname(AES.decrypt(medicSchedule.getMedic().getSurname()))
                        .specialization(medicSchedule.getSpecialization())
                        .build());
            }
        }
        return availableSpotsList;
    }

    private String isDataValidForAppointment(AppointmentRequest appointmentRequest, Integer accountId) {
        String response = "";
        boolean alreadyTaken = appointmentRepository.existsByMedic_IdAndSpecializationAndDayAndStartHour(appointmentRequest.getMedicId(), appointmentRequest.getSpecialization(), appointmentRequest.getDay()
                , LocalTime.of(Integer.parseInt(appointmentRequest.getStartHour().substring(0, 2)), Integer.parseInt(appointmentRequest.getStartHour().substring(3, 5))));
        if (alreadyTaken) {
            response += "Already taken!" + "\n";
        }
        Boolean isMedicWorking = medicScheduleRepository.existsByMedic_IdAndDayAndStartHour(appointmentRequest.getMedicId(), appointmentRequest.getDay(), LocalTime.of(8, 0));

        if (!isMedicWorking) {
            response += "Medic not working today!";
        }
        if (!patientService.existsByAccountId(accountId)) {
            response += "Only patients can make appointments!";
        }
        System.out.println(response);
        return response;
    }

    public String makeAppointment(AppointmentRequest appointmentRequest, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        String response = isDataValidForAppointment(appointmentRequest, account.getId());
        if (Objects.equals(response, "")) {


            Appointment appointment = appointmentRepository.save(Appointment
                    .builder()
                    .specialization(appointmentRequest.getSpecialization())
                    .ensured(appointmentRequest.getEnsured())
                    .medic(medicService.getMedicByMedicId(appointmentRequest.getMedicId()))
                    .patient(patientService.getPatientFromAccountId(account.getId()))
                    .day(appointmentRequest.getDay())
                            .completed(false)
                    .startHour(LocalTime.of(Integer.parseInt(appointmentRequest.getStartHour().substring(0, 2)), Integer.parseInt(appointmentRequest.getStartHour().substring(3, 5))))
                    .build());

            Medic medic = medicService.getMedicByMedicId(appointmentRequest.getMedicId());
            medic.getAppointments().add(appointment);
            medicService.saveMedicByMedic(medic);

            Patient patient = patientService.getPatientFromAccountId(account.getId());
            patient.getAppointments().add(appointment);
            patientService.savePatientByPatient(patient);

            return AppointmentResponse
                    .builder()
                    .id(appointment.getId())
                    .day(appointment.getDay())
                    .startHour(appointment.getStartHour())
                    .specialization(appointment.getSpecialization())
                    .medicSurname(AES.decrypt(appointment.getMedic().getSurname()))
                    .medicName(AES.decrypt(appointment.getMedic().getName()))
                    .patientSurname(AES.decrypt(appointment.getPatient().getSurname()))
                    .patientName(AES.decrypt(appointment.getPatient().getName()))
                    .response("Success")
                    .ensured(appointment.getEnsured())
                    .build()
                    .toString();
        }
        return AppointmentResponse.builder().response(response).build().toString();
    }

    public AppointmentsTodayResponse getMedicsAppointmentsFuture(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        if (medic == null) {
            return AppointmentsTodayResponse.builder().status("Only medics can see their appointments!").build();
        }
        List<Appointment> appointmentList = appointmentRepository.getAllByMedicIdAndDayIsGreaterThanEqualAndCompletedIsFalseAndStartHourIsAfter(medic.getId(), LocalDate.now().getDayOfMonth(),LocalTime.now());
        List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            appointmentResponsesList.add(AppointmentResponse
                    .builder()
                    .id(appointment.getId())
                    .specialization(appointment.getSpecialization())
                    .medicName(AES.decrypt(appointment.getMedic().getName()))
                    .medicSurname(AES.decrypt(appointment.getMedic().getSurname()))
                    .patientName(AES.decrypt(appointment.getPatient().getName()))
                    .patientSurname(AES.decrypt(appointment.getPatient().getSurname()))
                    .patientId(appointment.getPatient().getId())
                    .startHour(appointment.getStartHour())
                    .day(appointment.getDay())
                    .ensured(appointment.getEnsured())
                    .response("Success")
                    .completed(appointment.getCompleted())
                    .build());
        }
        return AppointmentsTodayResponse
                .builder()
                .appointments(appointmentResponsesList)
                .status("Success")
                .build();
    }

    public AppointmentsTodayResponse getMedicsAppointmentsPast(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        if (medic == null) {
            return AppointmentsTodayResponse.builder().status("Only medics can see their appointments!").build();
        }
        List<Appointment> appointmentList = appointmentRepository.getAllByMedicIdAndDayIsLessThanEqualAndCompletedIsTrue(medic.getId(), LocalDate.now().getDayOfMonth());
        List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            appointmentResponsesList.add(AppointmentResponse
                    .builder()
                    .id(appointment.getId())
                    .specialization(appointment.getSpecialization())
                    .medicName(AES.decrypt(appointment.getMedic().getName()))
                    .medicSurname(AES.decrypt(appointment.getMedic().getSurname()))
                    .patientName(AES.decrypt(appointment.getPatient().getName()))
                    .patientSurname(AES.decrypt(appointment.getPatient().getSurname()))
                    .patientId(appointment.getPatient().getId())
                    .startHour(appointment.getStartHour())
                    .day(appointment.getDay())
                    .ensured(appointment.getEnsured())
                    .response("Success")
                    .completed(appointment.getCompleted())
                    .build());
        }
        return AppointmentsTodayResponse
                .builder()
                .appointments(appointmentResponsesList)
                .status("Success")
                .build();
    }

    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentRepository.getAppointmentById(appointmentId);
    }

    public Boolean startAppointment(Integer appointmentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        Appointment appointment = this.getAppointmentById(appointmentId);
        Patient patient = appointment.getPatient();
        if (appointment.getMedic().getId().equals(medic.getId())) {
            if (!medicPatientService.isPatientEnrolledToMedic(medic.getId(), patient.getId())) {
                medicPatientService.enrollPatient(patient.getId(), request);
            }
            if(appointment.getConsultation() == null){
                ConsultationDocument consultationDocument = new ConsultationDocument();
                consultationDocument = consultationDocumentService.saveConsultationDocument(consultationDocument);

                Consultation consultation = new Consultation();
                consultation.setAppointment(appointment);
                consultation.setConsultationDocument(consultationDocument);
                consultation = consultationService.saveConsultation(consultation);

                appointment.setConsultation(consultation);

                AnalysisDocument analysisDocument = new AnalysisDocument();
                analysisDocument.setAppointment(appointment);
                analysisDocument = analysisDocumentService.saveAnalysisDocument(analysisDocument);

                appointment.setAnalysisDocument(analysisDocument);
                appointmentRepository.save(appointment);
            }
            return true;
        }
        return null;
    }

    public String cancelPatientAppointment(Integer appointmentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = patientService.getPatientFromAccountId(account.getId());
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        Medic medic = appointment.getMedic();
        if (appointment.getPatient().getId().equals(patient.getId())) {
            medic.getAppointments().remove(appointment);
            patient.getAppointments().remove(appointment);

            medicService.saveMedicByMedic(medic);
            patientService.savePatientByPatient(patient);
            appointmentRepository.delete(appointment);
            if (appointmentRepository.existsAppointmentById(appointmentId)) {
                String patientEmail = AES.decrypt(patient.getEmail());
                boolean sent = MailSender.patientCanceled(patientEmail, appointment);
                if (sent) {

                    return "Success";
                }
                return "Error at sending mail";

            } else {
                return "Error at deleting appointment";
            }
        }
        return "Error";
    }

    public String cancelMedicAppointment(Integer appointmentId, HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Medic medic = medicService.getMedicByAccountId(account.getId());
        Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
        Patient patient = appointment.getPatient();
        if (appointment.getMedic().getId().equals(medic.getId())) {
            medic.getAppointments().remove(appointment);
            patient.getAppointments().remove(appointment);

            medicService.saveMedicByMedic(medic);
            patientService.savePatientByPatient(patient);
            appointmentRepository.delete(appointment);
            if (appointmentRepository.existsAppointmentById(appointmentId)) {
                String patientEmail = AES.decrypt(patient.getEmail());
                boolean sent = MailSender.medicCanceled(patientEmail, appointment);
                if (sent) {

                    return "Success";
                }
                return "Error at sending mail";

            } else {
                return "Error at deleting appointment";
            }
        }
        return "Error";
    }

    public AppointmentsTodayResponse getFuturePatientsAppointments(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = patientService.getPatientFromAccountId(account.getId());

        if (patient == null) {
            return AppointmentsTodayResponse.builder().status("No patient found with this info!").build();
        }
        List<Appointment> appointmentList = appointmentRepository.getAllByPatient_IdAndDayGreaterThan(patient.getId(), LocalDate.now().getDayOfMonth());

        appointmentList.addAll(appointmentRepository.getAllByPatient_IdAndDayIsAndStartHourGreaterThanEqual(patient.getId(), LocalDate.now().getDayOfMonth(),LocalTime.now()));
        List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            appointmentResponsesList.add(AppointmentResponse
                    .builder()
                    .id(appointment.getId())
                    .specialization(appointment.getSpecialization())
                    .medicName(AES.decrypt(appointment.getMedic().getName()))
                    .medicSurname(AES.decrypt(appointment.getMedic().getSurname()))
                    .patientName(AES.decrypt(appointment.getPatient().getName()))
                    .patientSurname(AES.decrypt(appointment.getPatient().getSurname()))
                    .startHour(appointment.getStartHour())
                    .day(appointment.getDay())
                    .patientId(appointment.getPatient().getId())
                    .ensured(appointment.getEnsured())
                    .response("Success")
                    .completed(appointment.getCompleted())
                    .build());
        }
        return AppointmentsTodayResponse.builder()
                .appointments(appointmentResponsesList)
                .status("Success")
                .build();
    }

    public AppointmentsTodayResponse getPastPatientsAppointments(HttpServletRequest request) {
        Account account = jwtService.getAccountFromRequest(request);
        Patient patient = patientService.getPatientFromAccountId(account.getId());

        if (patient == null) {
            return AppointmentsTodayResponse.builder().status("No patient found with this info!").build();
        }
        List<Appointment> appointmentList = appointmentRepository.getAllByPatient_IdAndDayLessThanOrCompletedIsTrue(patient.getId(), LocalDate.now().getDayOfMonth());

        List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {

            AppointmentResponse a = AppointmentResponse
                    .builder()
                    .id(appointment.getId())
                    .specialization(appointment.getSpecialization())
                    .medicName(AES.decrypt(appointment.getMedic().getName()))
                    .medicSurname(AES.decrypt(appointment.getMedic().getSurname()))
                    .patientName(AES.decrypt(appointment.getPatient().getName()))
                    .patientSurname(AES.decrypt(appointment.getPatient().getSurname()))
                    .startHour(appointment.getStartHour())
                    .day(appointment.getDay())
                    .ensured(appointment.getEnsured())
                    .response("Success")
                    .build();
                    a.setResultDone(appointment.getAnalysisDocument().getAnalysisResultDocument().getCompleted());
                    appointmentResponsesList.add(a);
        }
        return AppointmentsTodayResponse.builder()
                .appointments(appointmentResponsesList)
                .status("Success")
                .build();
    }

    public String endAppointment(Integer appointmentId, MultipartFile file, String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(jsonData);
            String reason = jsonNode.get("reason").asText();
            String symptoms = jsonNode.get("symptoms").asText();
            String diagnosis = jsonNode.get("diagnosis").asText();
            String recomandations = jsonNode.get("recomandations").asText();

            Appointment appointment = appointmentRepository.getAppointmentById(appointmentId);
            String documentName = "Consultatie " + appointment.getSpecialization() + " " + LocalDate.now();

            ConsultationDocument consultationDocument = appointment.getConsultation().getConsultationDocument();
            consultationDocument.setData(file.getBytes());
            consultationDocument.setDocName(documentName);
            consultationDocument.setDocType(file.getContentType());

            consultationDocument = consultationDocumentService.saveConsultationDocument(consultationDocument);

            Consultation consultation = appointment.getConsultation();
            consultation.setAppointment(appointment);
            consultation.setConsultationDocument(consultationDocument);
            consultation.setReason(reason);
            consultation.setSymptoms(symptoms);
            consultation.setInitialDiagnosis(diagnosis);
            consultation.setRecommendations(recomandations);

            consultation = consultationService.saveConsultation(consultation);

            appointment.setConsultation(consultation);
            appointment.setCompleted(true);
            appointmentRepository.save(appointment);
            return "Success";
        } catch (IOException e) {
            return "Error";
        }
    }


    public Integer getPatientId(Integer appointmentId, HttpServletRequest request) {
        Appointment appointment = getAppointmentById(appointmentId);
        return appointment.getPatient().getId();

    }



    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }
}
