function getAllAnalysisNotCompleted() {
  const url = "http://localhost:8080/api/analysis-document/allNotCompleted"; // Replace with your backend API endpoint URL
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
  const analysisListContainer = document.getElementById("nurse-analysis-list");

  analysisListContainer.innerHTML = ""; // Clear existing content
  analysisList.forEach((analysis) => {
    if (!analysis.completed) {
      const analysisDiv = createAnalysisDiv(analysis);
      analysisListContainer.appendChild(analysisDiv);
    }
  });
}

function createAnalysisDiv(analysis) {
  console.log(analysis)
  const analysisDiv = document.createElement("div");
  analysisDiv.className = "analysis";
  analysisDiv.id = "analysis-" + analysis.analysisDocumentId;
  const appointmentDetails = `
          <br>
          <p id = "name-patient">Numele pacientului: ${analysis.patientName} ${analysis.patientSurname}</p>
          <p id = "name-medic">Numele medicului: ${analysis.medicName} ${analysis.medicSurname}</p>
          <p id = "specialization">Specializarea: ${analysis.specialization}</p>
          <button onclick="finishAnalysis(${analysis.appointmentId})">Incheie recoltarea analizelor</button>
          <button onclick="nurseDownloadFile('${analysis.appointmentId}', '${analysis.patientName}', '${analysis.patientSurname}')">Download</button>
          
          `;
  // Make sure analysisRequired is an array

  analysisDiv.innerHTML = appointmentDetails;

  // Create the analysis list
  const analysisList = document.createElement("ul");
  for (const analysisObject of analysis.analysisRequired) {
    const listItem = document.createElement("li");
    listItem.textContent = `${analysisObject.name} - ${analysisObject.analysisCategory.name}`;
    analysisList.appendChild(listItem);
  }

  // Add the analysis list to the analysis div
  analysisDiv.appendChild(analysisList);

  return analysisDiv;
}

function finishAnalysis(appointmentId){
    const token = localStorage.getItem("jwtToken");
  
    // Create fetch options including headers
    var options = {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + token, // Set the Authorization header with the bearer token
      },
    };
  
    // Make the fetch request to the API endpoint that returns the file
    fetch('http://localhost:8080/api/analysis-document/complete/' + appointmentId, options) 
      .then(function(response) {
        if (response.ok) {
            location.reload();
        } else {
          
        }
      })
      
}

function nurseDownloadFile(appointmentId,patientName,patientSurname){
  console.log(appointmentId)
    // Get the bearer token (replace with your actual token)
    const token = localStorage.getItem("jwtToken");
  
    // Create fetch options including headers
    var options = {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + token, // Set the Authorization header with the bearer token
      },
    };
  
    // Make the fetch request to the API endpoint that returns the file
    fetch('http://localhost:8080/api/analysis-document/download/' + appointmentId, options) 
      .then(function(response) {
        if (response.ok) {
          // Request was successful
          return response.blob(); // Get the response body as a Blob
        } else {
          // Request failed
          throw new Error('File download failed: ' + response.status);
        }
      })
      .then(function(blob) {
        // Create a download link for the Blob data
        var downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = "Analize" + patientName + patientSurname; // Set the desired filename for the downloaded file
        downloadLink.click(); // Trigger the download
      })
      .catch(function(error) {
        // Request error
        console.error(error);
        // Perform error handling here, if needed
      });
}
