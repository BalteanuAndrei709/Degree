function getPatientAppointments() {
  const url = "http://localhost:8080/api/appointment/Patients/past"; // Replace with your backend API endpoint URL
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
    "past-patient-appointments-container"
  );
  appointmentsContainer.innerHTML = ""; // Clear existing content
  appointments.forEach((appointment) => {
    const appointmentDiv = createAppointmentDiv(appointment);
    appointmentsContainer.appendChild(appointmentDiv);
  });
}

// Function to create appointment div with buttons
function createAppointmentDiv(appointment) {
  console.log(appointment)
  const appointmentDiv = document.createElement("div");
  appointmentDiv.className = "appointment";
  const appointmentDetails = `
        <p>Appointment ID: ${appointment.id}</p>
        <p>Medic FullName: ${appointment.medicName} ${appointment.medicSurname}</p>
        <p>Specialization: ${appointment.specialization}</p>
        <p>Ensured: ${appointment.ensured}</p>
        <p>Date: ${appointment.day}</p>
        <p>Time: ${appointment.startHour}</p>
        
        <button onclick="downloadConsultationConclusion(${appointment.id})">Descarca pdf consultatiei</button>
        <button onclick="downloadResultAnalysis(${appointment.id},'${appointment.patientName}' , '${appointment.patientSurname}')" ${appointment.resultDone ? '' : 'disabled'} style="${appointment.resultDone ? '' : 'display: none;'}">Descarca pdf rezultate</button>

        
        `;
  appointmentDiv.innerHTML = appointmentDetails;

  return appointmentDiv;
}

function downloadConsultationConclusion(appointmentId) {
  const url = `http://localhost:8080/api/consultation/download/${appointmentId}`; // Replace with your backend API endpoint URL
  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  // Create fetch options including headers
  var options = {
    method: "GET",
    headers: {
      Authorization: "Bearer " + token, // Set the Authorization header with the bearer token
    },
    mode: "cors",
  };

  // Make the fetch request to the API endpoint that returns the file
  fetch(url, options)
    .then(function (response) {
      console.log(response.headers);
      if (response.ok) {
        return response.blob();
      } else {
        throw new Error("File download failed: " + response.status);
      }
    })
    .then(async function (blob) {
      const filename = await getFilenameFromResponseHeaders(appointmentId);
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = filename;
      a.click();
    })
    .catch(function (error) {
      console.error(error);
    });
}

async function getFilenameFromResponseHeaders(appointmentId) {
  const response = await fetch(
    `http://localhost:8080/api/consultation/getName/${appointmentId}`,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
      },
    }
  );

  if (!response.ok) {
    throw new Error(`Failed to get consultation filename: ${response.status}`);
  }

  const data = await response.json();
  return data.response;
}

function downloadResultAnalysis(appointmentId,name, surname) {
  const url = `http://localhost:8080/api/analysis-document/download/result/${appointmentId}`; // Replace with your backend API endpoint URL
  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  // Create fetch options including headers
  var options = {
    method: "GET",
    headers: {
      Authorization: "Bearer " + token, // Set the Authorization header with the bearer token
    },
    mode: "cors",
  };

  // Make the fetch request to the API endpoint that returns the file
  fetch(url, options)
    .then(function (response) {
      console.log(response.headers);
      if (response.ok) {
        return response.blob();
      } else {
        throw new Error("File download failed: " + response.status);
      }
    })
    .then(async function (blob) {
      const filename = name+surname+"RezultateAnalize"
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = filename;
      a.click();
    })
    .catch(function (error) {
      console.error(error);
    });
}
