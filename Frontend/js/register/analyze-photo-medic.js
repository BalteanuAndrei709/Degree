document.addEventListener("DOMContentLoaded", function () {
    document
      .getElementById("analyze-photo-medic-form")
      .addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent form submission
  
        const formData = new FormData();
        // Get file from input field
        const fileInput = document.getElementById("medic-photo");
        const file = fileInput.files[0]; // Get the first file selected
  
        formData.append("file", file); // Append file to form data
  
        // Make fetch request with FormData
        fetch("http://localhost:8080/api/registration/analyze-photo", {
          method: "POST",
          body: formData,
        })
          .then((response) => {
            if (response.ok) {
              return response.json(); // Parse response as JSON
            } else {
              throw new Error("Failed to register"); // Throw error for failed response
            }
          })
          .then((data) => {
            // Populate form fields with user info from API response
            document.getElementById("socialId").value = data.socialId;
           
            document.getElementById("name").value = data.name;
            document.getElementById("surname").value = data.surname;
            document.getElementById("age").value = data.age;
  
            const gender = data.gender; // Assuming gender is a string value
            if (gender === "Male") {
              document.getElementById("male").checked = true;
              document.getElementById("female").checked = false;
            } else if (gender === "Female") {
              document.getElementById("male").checked = false;
              document.getElementById("female").checked = true;
            }
  
            document.getElementById("county").value = data.county;
          })
          .catch((error) => {
            // Handle fetch error or failed registration
            console.error(error);
          });
      });
  });
  