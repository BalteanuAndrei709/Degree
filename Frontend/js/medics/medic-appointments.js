function getMedicAppointments() {
    const url = 'http://localhost:8080/api/appointment/Medics/Today'; // Replace with your backend API endpoint URL
    const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token
  
    // Create a headers object with Authorization header containing the bearer token
    const headers = new Headers();
    headers.append('Authorization', `Bearer ${token}`);
  
    // Create a request object with the URL and headers
    const request = new Request(url, {
      method: 'GET',
      headers: headers,
      mode: 'cors'
    });
  
    // Fetch the request and handle the response
    fetch(request)
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        // Handle the JSON response data
        console.log('Response:', data);
        // Call the function to create and append appointment divs
        createAppointmentDivs(data.appointments);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

getMedicAppointments();
  
  // Function to create and append appointment divs to DOM
  function createAppointmentDivs(appointments) {
    const appointmentsContainer = document.getElementById('medic-appointments-container');
    appointmentsContainer.innerHTML = ''; // Clear existing content
    appointments.forEach(appointment => {
      const appointmentDiv = createAppointmentDiv(appointment);
      appointmentsContainer.appendChild(appointmentDiv);
    });
  }
  
  // Function to create appointment div with buttons
  function createAppointmentDiv(appointment) {
    const appointmentDiv = document.createElement('div');
    appointmentDiv.className = 'appointment';
    const appointmentDetails = `
      <p>Appointment ID: ${appointment.id}</p>
      <p>Patient FullName: ${appointment.patientName} ${appointment.patientSurname}</p>
      <p>Ensured: ${appointment.ensured}</p>
      <p>Date: ${appointment.day}</p>
      <p>Time: ${appointment.startHour}</p>
     
      <button onclick="cancelAppointment(${appointment.id})">Cancel</button>
      <button onclick="start(${appointment.id})">Start</button>
    `;
    appointmentDiv.innerHTML = appointmentDetails;
    return appointmentDiv;
  }
  
  // Function to cancel an appointment
  function cancelAppointment(appointmentId) {
    // Replace this with your actual code to cancel the appointment
    console.log(`Cancelled appointment with ID: ${appointmentId}`);
  }
  
  // Function to view appointment details
  function start(appointmentId) {
    // Replace this with your actual code to view appointment details
    console.log(`Start: ${appointmentId}`);
  }
  