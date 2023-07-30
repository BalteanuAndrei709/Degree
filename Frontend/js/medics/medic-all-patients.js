document.addEventListener("DOMContentLoaded", function () {
  const mainElem = document.querySelector("main");
  const patientsListElem = document.getElementById("medic-all-patients-list");

  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  fetch("http://localhost:8080/api/medic/allPatients", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.response === "Success") {
        console.log(data);
        const patients = data.patientList;

        patients.forEach((patient) => {
          const patientElem = document.createElement("div");
          patientElem.classList.add("patient");
          patientElem.innerHTML = `
          <i class="fas fa-user"></i><p id="name-patient" class = "patient-name">Numele:${patient.name} ${patient.surname} </p>
          <i class="fas fa-birthday-cake"></i><p id="patient-age" class="patient-age">Vârsta: ${patient.age}</p>
          <i class="fas fa-venus-mars"></i><p id="patient-gender" class="patient-gender">Sex: ${patient.gender}</p>
          <i class="fas fa-id-card"></i><p id="patient-cnp" class="patient-cnp">C.N.P.: ${patient.socialId}</p>
          <i class="fas fa-map-marker-alt"></i><p id="patient-county" class="patient-county">Județ: ${patient.county}</p>
          <button onclick="getPatientPastAppointmentsDocuments(${patient.patientId})">Consultații anterioare</button>
          <button onclick="getPatientDocument(${patient.patientId})">Documentele pacientului</button>
        `;

          if (patientsListElem) {
            patientsListElem.appendChild(patientElem);
          } else {
            console.log("Could not find patients list element");
          }
        });
      }
    })
    .catch((error) => console.log(error));
});

function getPatientPastAppointmentsDocuments(patientId){
    localStorage.setItem("patientId",patientId)
    window.location.href ="http://127.0.0.1:5500/medics/medic-patient-consultations.html"
}

function getPatientDocument(patientId){
    localStorage.setItem("patientId",patientId)
    window.location.href ="http://127.0.0.1:5500/medics/medic-patient-documents.html"
}
