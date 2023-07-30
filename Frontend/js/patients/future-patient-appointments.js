function getPatientAppointments() {
  const url = "http://localhost:8080/api/appointment/Patients/future"; // Replace with your backend API endpoint URL
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
      // Handle the JSON response data
      console.log("Response:", data);
      // Call the function to create and append appointment divs
      createAppointmentDivs(data.appointments);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

getPatientAppointments();

// Function to create and append appointment divs to DOM
function createAppointmentDivs(appointments) {
  const appointmentsContainer = document.getElementById(
    "future-patient-appointments-container"
  );
  appointmentsContainer.innerHTML = ""; // Clear existing content
  appointments.forEach((appointment) => {
    if(!appointment.completed){
    const appointmentDiv = createAppointmentDiv(appointment);
    appointmentsContainer.appendChild(appointmentDiv);
    }
  });
}

// Function to create appointment div with buttons
function createAppointmentDiv(appointment) {
  const appointmentDiv = document.createElement("div");
  appointmentDiv.className = "appointment";
  const appointmentDetails = `
      <p>Numele medicului: ${appointment.medicName} ${appointment.medicSurname}</p>
      <p>Specializarea: ${appointment.specialization}</p>
      <p>Asigurat: ${appointment.ensured}</p>
      <p>Data: ${appointment.day}</p>
      <p>Ora: ${appointment.startHour}</p>
      
     
      <button onclick="cancelAppointment(${appointment.id})">Cancel</button>
    `;
  appointmentDiv.innerHTML = appointmentDetails;
  return appointmentDiv;
}

// Function to cancel an appointment
function cancelAppointment(appointmentId) {
  const url = `http://localhost:8080/api/appointment/patient/cancel/${appointmentId}`; // Replace with your backend API endpoint URL
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


