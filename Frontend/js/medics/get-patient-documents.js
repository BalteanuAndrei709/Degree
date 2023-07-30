function getPatientDocuments() {
    let patientId = localStorage.getItem("patientId");
    localStorage.removeItem("patientId")
    const url = "http://localhost:8080/api/file/documentsOfPatient?patientId=" + patientId; // Replace with your backend API endpoint URL
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
        createPatientDocumentDivs(data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
}

getPatientDocuments();

function createPatientDocumentDivs(patientDocuments){
    const documentContainer = document.getElementById(
        "medic-patient-document-list"
      );
      
      documentContainer.innerHTML = ""; // Clear existing content
      patientDocuments.forEach((document) => {
        const documentDiv = createMedicDiv(document);
        documentContainer.appendChild(documentDiv);
      });
}

function createMedicDiv(patient_document){
    const documentDiv = document.createElement("div");
    documentDiv.className = "document";
    const medicDetails = `
        <p>Numele documentului: ${patient_document.name}</p>
        <p>Tip: ${patient_document.type}</p>

        <button onclick="medicDownloadFile('${patient_document.id}', '${patient_document.name}')">Download</button>
        `;
        
        documentDiv.innerHTML = medicDetails;
    return documentDiv;
}

function medicDownloadFile(documentId,documentName) {
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
    fetch('http://localhost:8080/api/file/medic/downloadFile?documentId=' + documentId , options) 
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
        downloadLink.download = documentName; // Set the desired filename for the downloaded file
        downloadLink.click(); // Trigger the download
      })
      .catch(function(error) {
        // Request error
        console.error(error);
        // Perform error handling here, if needed
      });
}

function patientRenameFile(documentId, documentName) {
  // Prompt the user for a new filename
  var newFilename = prompt("Enter a new filename:", documentName);
  if (!newFilename) {
    // User cancelled or did not enter a filename
    return;
  }

  // Get the bearer token (replace with your actual token)
  var bearerToken = localStorage.getItem("jwtToken");

  // Create fetch options including headers
  var options = {
    method: 'POST', // Use PUT method for updating the document name
    headers: {
      'Authorization': 'Bearer ' + bearerToken, // Set the Authorization header with the bearer token
      'Content-Type': 'application/json', // Set the Content-Type header for the request body
    },
    body: JSON.stringify({ documentId:documentId, newFilename: newFilename }), // Set the new filename in the request body as JSON
  };

  // Make the fetch request to the API endpoint to update the document name
  fetch('http://localhost:8080/api/file/rename' , options) // Replace with your actual endpoint URL and document ID
    .then(function(response) {
      if (response.ok) {
        // Request was successful
        console.log('File renamed successfully!');
        location.reload()
      } else {
        // Request failed
        throw new Error('File renaming failed: ' + response.status);
      }
    })
    .catch(function(error) {
      // Request error
      console.error(error);
      // Perform error handling here, if needed
    });
}
  