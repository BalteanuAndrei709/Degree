function getMedicAppointments() {
  const url = "http://localhost:8080/api/appointment/Medics/future"; // Replace with your backend API endpoint URL
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
    "medic-appointment-list"
  );
  console.log(medicAppintments);
  appointmentContainer.innerHTML = ""; // Clear existing content
  medicAppintments.forEach((appointment) => {
    if (!appointment.completed) {
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
        <p>Numele pacientului: ${appointment.patientName} ${appointment.patientSurname}</p>
        <p>Specializarea: ${appointment.specialization}</p>
        <p>Asigurat: ${appointment.ensured}</p>
        <p>Data: ${appointment.day}</p>
        <p>Ora: ${appointment.startHour}</p>
        <button onclick="medicCancelAppointment(${appointment.id})">Anuleaza</button>
        <button onclick="startAppointment(${appointment.id},${appointment.patientId})">Incepe programarea</button>
        
        `;
  appointmentDiv.innerHTML = appointmentDetails;

  return appointmentDiv;
}

function medicCancelAppointment(appointmentId) {
  const url = `http://localhost:8080/api/appointment/medic/cancel/${appointmentId}`; // Replace with your backend API endpoint URL
  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  // Create a headers object with Authorization header containing the bearer token
  const headers = new Headers();
  headers.append("Authorization", `Bearer ${token}`);

  // Create a request object with the URL and headers
  const request = new Request(url, {
    method: "POST",
    headers: headers,
    mode: "cors",
  });

  // Fetch the request and handle the response
  fetch(request)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      console.log(`Cancelled appointment with ID: ${appointmentId}`);
      // Refresh the page to update the UI
      location.reload();
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

function startAppointment(appointmentId,patientId) {
  localStorage.setItem("appointmentId", appointmentId);
  localStorage.setItem("patientId", patientId);
  const dataPromise = enrollPatient(patientId);
  dataPromise.then((data) => {
    
    if (data.response != "Error") {
      window.location.href = "../medics/medic-start-appointment.html";
    }
  });
}

function enrollPatient(patientId) {
  let token = localStorage.getItem("jwtToken");
  return fetch(
    `http://localhost:8080/api/appointment/enrollPatient?patientId=${patientId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  )
    .then((response) => response.json())
    .then((data) => {
      return data;
    })
    .catch((error) => console.error(error));
}
