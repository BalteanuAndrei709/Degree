function getPatientDocuments() {
  const url = "http://localhost:8080/api/file/all"; // Replace with your backend API endpoint URL
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
      createDocumentPatientDivs(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

getPatientDocuments();

function createDocumentPatientDivs(patientDocuments) {
  const documentContainer = document.getElementById("patient-document-list");

  documentContainer.innerHTML = ""; // Clear existing content
  patientDocuments.forEach((document) => {
    const documentDiv = createMedicDiv(document);
    documentContainer.appendChild(documentDiv);
  });
}

function createMedicDiv(patient_document) {
  const documentDiv = document.createElement("div");
  documentDiv.className = "document";
  const medicDetails = `
        <p>Document ID: ${patient_document.id}</p>
        <p>Numele documentului: ${patient_document.name}</p>
        <p>Tip: ${patient_document.type}</p>

        <button onclick="patientDownloadFile('${patient_document.id}', '${patient_document.name}')" id="download-button">Descarca</button>
        <button onclick="patientRenameFile(${patient_document.id}, '${patient_document.name}')" id="rename-button">Redenumeste</button>
        <button onclick="patientDeleteFile(${patient_document.id})" id="delete-button">Sterge</button>


        `;

  documentDiv.innerHTML = medicDetails;
  return documentDiv;
}

function patientDownloadFile(documentId, documentName) {
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
  fetch("http://localhost:8080/api/file/download/" + documentId, options)
    .then(function (response) {
      if (response.ok) {
        // Request was successful
        return response.blob(); // Get the response body as a Blob
      } else {
        // Request failed
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

function patientRenameFile(documentId, documentName) {
  // Show the modal form
  var modal = document.getElementById("modal");
  modal.style.display = "block";

  // Get references to the input and buttons in the modal form
  var newFilenameInput = document.getElementById("new-filename-input");
  var saveButton = document.getElementById("modal-save-button");
  var cancelButton = document.getElementById("modal-cancel-button");

  // Set the initial value of the input to the current document name
  newFilenameInput.value = documentName;

  cancelButton.addEventListener("click", function () {
    modal.style.display = "none";
  })
  // Add event listeners to the buttons
  saveButton.addEventListener("click", function () {
    // Get the new filename from the input
    var newFilename = newFilenameInput.value;
    if (!newFilename) {
      // User did not enter a filename
      return;
    }

    // Get the bearer token (replace with your actual token)
    var bearerToken = localStorage.getItem("jwtToken");

    // Create fetch options including headers
    var options = {
      method: "POST", // Use PUT method for updating the document name
      headers: {
        Authorization: "Bearer " + bearerToken, // Set the Authorization header with the bearer token
        "Content-Type": "application/json", // Set the Content-Type header for the request body
      },
      body: JSON.stringify({
        documentId: documentId,
        newFilename: newFilename,
      }), // Set the new filename in the request body as JSON
    };

    // Make the fetch request to the API endpoint to update the document name
    fetch("http://localhost:8080/api/file/rename", options) // Replace with your actual endpoint URL and document ID
      .then(function (response) {
        if (response.ok) {
          // Request was successful
          console.log("File renamed successfully!");
          location.reload();
        } else {
          // Request failed
          throw new Error("File renaming failed: " + response.status);
        }
      })
      .catch(function (error) {
        // Request error
        console.error(error);
        // Perform error handling here, if needed
      });
  });
}

function patientDeleteFile(documentId) {
  const url = "http://localhost:8080/api/file/delete";
  const data = { id: documentId };
  let token = localStorage.getItem("jwtToken");
  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((result) => location.reload())
    .catch((error) => console.error("Error:", error));
}
