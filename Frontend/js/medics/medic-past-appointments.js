function getMedicAppointments() {
  const url = "http://localhost:8080/api/appointment/Medics/past"; // Replace with your backend API endpoint URL
  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  // Create a headers object with Authorization header containing the bearer token
  const headers = new Headers();
  headers.append("Authorization", `Bearer ${token}`);

  // Create a request object with the URL and headers
  const request = new Request(url, {
    method: "GET",
    headers: headers,
    mode: "cors",
  });

  // Fetch the request and handle the response
  fetch(request)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      createAppointmentMedicDiv(data.appointments);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

getMedicAppointments();

function createAppointmentMedicDiv(medicAppintments) {
  const appointmentContainer = document.getElementById(
    "medic-past-appointment-list"
  );
  console.log(medicAppintments);
  appointmentContainer.innerHTML = ""; // Clear existing content
  medicAppintments.forEach((appointment) => {
    if (appointment.completed) {
      const appointmentDiv = createAppointmentDiv(appointment);
      appointmentContainer.appendChild(appointmentDiv);
    }
  });
}

function createAppointmentDiv(appointment) {
  const appointmentDiv = document.createElement("div");
  appointmentDiv.className = "appointment";
  appointmentDiv.id = "appointment-" + appointment.id;
  const appointmentDetails = `
          <p class="info-id">Id Programare: ${appointment.id}</p>
          <p class="patient-name">Nume: ${appointment.patientName} ${appointment.patientSurname}</p>
          <p class="info-patient-id">Id Pacientului: ${appointment.patientId}</p>
          <p class="info-ensured">Asigurat: ${appointment.ensured}</p>
          <p class="info-date">Data: ${appointment.day}</p>
          <p class="info-time">Ora: ${appointment.startHour}</p>
          <button onclick="modifyConsultation(${appointment.id})">Modifica documentul consultatiei</button>
          `;
  appointmentDiv.innerHTML = appointmentDetails;

  return appointmentDiv;
}

function modifyConsultation(appointmentId){
    console.log(appointmentId)
    localStorage.setItem("appointmentId", appointmentId);
    window.location.href= "../../medics/medic-modify-consultation.html"
}
