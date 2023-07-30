function getAllAnalysisNotCompleted() {
  const url =
    "http://localhost:8080/api/analysis-document/results/allNotCompleted"; // Replace with your backend API endpoint URL
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
      createAnalysisDivs(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
getAllAnalysisNotCompleted();

function createAnalysisDivs(analysisList) {
  console.log(analysisList);
  const container = document.getElementById("nurse-finish-analysis-list");

  // Loop through each appointment and create a div element with an upload file button
  analysisList.forEach((appointment) => {
    const div = document.createElement("div");
    div.classList.add("appointment");

    const pPatient = document.createElement("p");
    pPatient.innerText = `Numele pacientului:${appointment.patientName} ${appointment.patientSurname}`;
    pPatient.classList.add("patient-name");
    div.appendChild(pPatient);

    const pMedic = document.createElement("p");
    pMedic.innerText = `Numele medicului:${appointment.medicName} ${appointment.medicSurname}`;
    pMedic.classList.add("medic-name");
    div.appendChild(pMedic);

    const pDay = document.createElement("p");
    pDay.innerText = `Ziua programarii:${appointment.day} `;
    pDay.classList.add("appointment-day");
    div.appendChild(pDay);

    const requiredAnalysisList = document.createElement("ul");
    requiredAnalysisList.innerText = "Analizele cerute:";
    requiredAnalysisList.classList.add("required-analysis-list");
    appointment.analysisRequired.forEach((requiredAnalysis) => {
      const requiredAnalysisItem = document.createElement("li");
      requiredAnalysisItem.innerText = requiredAnalysis.name;
      requiredAnalysisItem.classList.add("required-analysis-item");
      requiredAnalysisList.appendChild(requiredAnalysisItem);
    });
    div.appendChild(requiredAnalysisList);

    const input = document.createElement("input");
    input.type = "file";
    input.classList.add("file-input");
    input.addEventListener("change", () => {
      const file = input.files[0];
      uploadFile(file, appointment.appointmentId);
    });
    div.appendChild(input);

    container.appendChild(div);
  });

  // Function to upload a file to the other API endpoint
  function uploadFile(file, appointmentId) {
    console.log(appointmentId);
    const formData = new FormData();
    formData.append("file", file);

    let token = localStorage.getItem("jwtToken");
    const headers = new Headers();
    headers.append("Authorization", `Bearer ${token}`);

    fetch(
      `http://localhost:8080/api/analysis-document/upload/results/${appointmentId}`,
      {
        method: "POST",
        headers: headers,
        body: formData,
      }
    )
      .then((response) => {
        console.log(response);
        location.reload();
        alert("File uploaded successfully!");
      })
      .catch((error) => {
        console.error(error);
        alert("File upload failed!");
      });
  }
}
