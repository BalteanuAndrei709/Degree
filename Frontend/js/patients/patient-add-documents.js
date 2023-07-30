// Attach an event listener to the form submission
document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("patient-add-document")
    .addEventListener("submit", function (event) {
      event.preventDefault(); // Prevent form submission

      // Get the file input element
      var fileInput = document.getElementById("patient-document");
      var file = fileInput.files[0]; // Get the selected file

      // Get the bearer token (replace with your actual token)
      var bearerToken = localStorage.getItem("jwtToken");

      // Create a FormData object to store the file data
      var formData = new FormData();
      formData.append("file", file);

      // Create fetch options including headers and body
      var options = {
        method: "POST",
        headers: {
          Authorization: "Bearer " + bearerToken, // Set the Authorization header with the bearer token
        },
        mode: "cors",
        body: formData,
      };

      // Make the fetch request
      fetch("http://localhost:8080/api/file/upload", options) // Replace with your actual endpoint URL
        .then(function (response) {
          if (response.ok) {
            // Request was successful
            return response.json(); // You can parse the response body as needed
          } else {
            // Request failed
            throw new Error("File upload failed: " + response.status);
          }
        })
        .then(function (data) {
          // Perform additional actions with the response data here, if needed
          console.log("File upload successful:", data);
          window.location.href="../patients/patient-documents.html"

        })
        .catch(function (error) {
          // Request error or JSON parsing error
          console.error(error);
          // Perform error handling here, if needed
        });
    });
});
