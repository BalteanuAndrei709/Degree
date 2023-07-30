function getPatientConsultations() {
  let patientId = localStorage.getItem("patientId");
  localStorage.removeItem("patientId")
  const url =
    "http://localhost:8080/api/consultation/ofPatient?patientId=" + patientId; // Replace with your backend API endpoint URL
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
      console.log(data);
      createPatientConsultationDivs(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

getPatientConsultations();

function createPatientConsultationDivs(patientConsultations) {
  const consultationContainer = document.getElementById(
    "medic-patient-consultation-list"
  );

  consultationContainer.innerHTML = ""; // Clear existing content
  patientConsultations.forEach((consultation) => {
    const documentDiv = createConsultationDiv(consultation);
    consultationContainer.appendChild(documentDiv);
  });
}

function createConsultationDiv(patient_consultation) {
  const documentDiv = document.createElement("div");
  documentDiv.className = "document";
  const medicDetails = `
    <div class="icon"><i class="fas fa-file-pdf"></i></div>
      <div class="details">
        <p>Numele documentului: ${patient_consultation.name}</p>
        <p>Tip: ${patient_consultation.type}</p>
        <p>Numele medicului: ${patient_consultation.medicName}  ${patient_consultation.medicSurname}</p>
        <p>Specializarea: ${patient_consultation.specialization}</p>
        <button onclick="medicDownloadFile('${patient_consultation.id}', '${patient_consultation.name}')">Download</button>
        <button onclick="downloadResultAnalysis(${patient_consultation.appointmentId},'${patient_consultation.name}' , '${patient_consultation.surname}')" ${patient_consultation.resultDone ? '' : 'disabled'} style="${patient_consultation.resultDone ? '' : 'display: none;'}">Descarca pdf rezultate</button>
        </div>
        
        `;

  documentDiv.innerHTML = medicDetails;
  return documentDiv;
}


function medicDownloadFile(documentId, documentName) {
  console.log(documentId)
  // Get the bearer token (replace with your actual token)
  const token = localStorage.getItem("jwtToken");

  // Create fetch options including headers
  var options = {
    method: "GET",
    headers: {
      Authorization: "Bearer " + token, // Set the Authorization header with the bearer token
    },
  };

  // Make the fetch request to the API endpoint that returns the file
  fetch(
    "http://localhost:8080/api/consultation/download?consultationId=" +
      documentId,
    options
  )
    .then(function (response) {
      if (response.ok) {
        // Request was successful
        return response.blob(); // Get the response body as a Blob
      } else {
        // Request failed
        console.log(response)
        throw new Error("File download failed: " + response.status);
      }
    })
    .then(function (blob) {
      // Create a download link for the Blob data
      var downloadLink = document.createElement("a");
      downloadLink.href = URL.createObjectURL(blob);
      downloadLink.download = documentName; // Set the desired filename for the downloaded file
      downloadLink.click(); // Trigger the download
    })
    .catch(function (error) {
      // Request error
      console.error(error);
      // Perform error handling here, if needed
    });
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
      const filename = "RezultateAnalize" + appointmentId
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = filename;
      a.click();
    })
    .catch(function (error) {
      console.error(error);
    });
}