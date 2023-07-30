function getPatientMedics() {
    const url = "http://localhost:8080/api/patient/myMedics"; // Replace with your backend API endpoint URL
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
        createMedicsPatientDivs(data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }
  
  getPatientMedics();
  
  // Function to create and append appointment divs to DOM
  function createMedicsPatientDivs(medicsPatient) {
    const medicContainer = document.getElementById(
      "patient-medics-list"
    );
    
    medicContainer.innerHTML = ""; // Clear existing content
    medicsPatient.forEach((medic) => {
      const appointmentDiv = createMedicDiv(medic);
      medicContainer.appendChild(appointmentDiv);
    });
  }
  
  // Function to create appointment div with buttons
  function createMedicDiv(medic) {
    const medicDiv = document.createElement("div");
    medicDiv.className = "appointment";
    const medicDetails = `
        <p>Medic ID: ${medic.id}</p>
        <p>Numele complet al medicului: ${medic.name}  ${medic.surname}</p>
        <p>Specializarea: ${medic.specialization}</p>

        <button onclick="unrollFromMedic(${medic.id})">Elimina</button>
        `;
        medicDiv.innerHTML = medicDetails;
    return medicDiv;
  }
  

  function unrollFromMedic(medicId) {
    const url = `http://localhost:8080/api/patient/unroll/${medicId}`; 
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
        
        // Refresh the page to update the UI
        location.reload();
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }
  

  
  
  