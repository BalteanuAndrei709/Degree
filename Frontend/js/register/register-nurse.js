// Retrieve form data on button click
document.addEventListener("DOMContentLoaded", function () {
    document
      .getElementById("register-nurse-form")
      .addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent form submission
  
        // Get form values
        const socialId = document.getElementById("socialId").value;
        if (!/^\d{13}$/.test(socialId)) {
          document.getElementById("errorSocialId").textContent =
            "Invalid socialId. Please enter a 14-digit number.";
          return; // Exit function if socialId is invalid
        } else {
          document.getElementById("errorSocialId").textContent = ""; // Clear error message if socialId is valid
        }
  
        const email = document.getElementById("email").value;
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regular expression for email validation
        if (!emailPattern.test(email)) {
          document.getElementById("errorEmail").textContent =
            "Invalid email. Please enter a valid email address.";
          return; // Exit function if email is invalid
        } else {
          document.getElementById("errorMail").textContent = ""; // Clear error message if email is valid
        }
  
        //CHECK NAME IS VALID
        const name = document.getElementById("name").value;
        var namePattern = /^[A-Za-z\s-]+$/; // Regular expression for letters, spaces, and hyphens
        if (!namePattern.test(name)) {
          document.getElementById("errorName").textContent =
            "Invalid name. Please enter a valid name.";
          return; // Exit function if name is invalid
        } else {
          document.getElementById("errorName").textContent = ""; // Clear error message if name is valid
        }
  
        const surname = document.getElementById("surname").value;
        var surnamePattern = /^[A-Za-z\s-]+$/; // Regular expression for letters, spaces, and hyphens
        if (!surnamePattern.test(surname)) {
          document.getElementById("errorSurname").textContent =
            "Invalid surname. Please enter a valid name.";
          return; // Exit function if name is invalid
        } else {
          document.getElementById("errorSurname").textContent = ""; // Clear error message if name is valid
        }
  
        //CHECK AGE IS VALID
        const age = document.getElementById("age").value;
        
        // Check if age is a valid number between 0 and 150
        if (age < 0 || age > 150) {
          // Display an error message to the user
          document.getElementById("errorAge").textContent =
            "Please enter a valid age between 0 and 150";
          
          return; // Stop form submission
        }
  
        const gender = document.querySelector(
          'input[name="gender"]:checked'
        ).value;
        const county = document.getElementById("county").value;
  
        const licenseId = document.getElementById("licenseId").value;
        console.log(licenseId)
        if (!/^\d{16}$/.test(licenseId)) {
          document.getElementById("errorLicenseId").textContent =
            "Invalid licenseId. Please enter a 16-digit number.";
          return; // Exit function if socialId is invalid
        } else {
          document.getElementById("errorLicenseId").textContent = ""; // Clear error message if socialId is valid
        }
        const specialization = document.getElementById("specialization").value;
        // Create request body
        const data = {
          licenseId,
          specialization,
          socialId,
          email,
          name,
          surname,
          age,
          gender,
          county,
        };
  
        // Get bearer token (replace with your own implementation)
        
        const jsonData = JSON.stringify(data);
        console.log(jsonData)
        // Make POST request to server with bearer token
        const response = await fetch(
          "http://localhost:8080/api/registration/nurse",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body:jsonData,
          }
        );
  
        // Check response status
        if (response.ok) {
          alert("Registration successful!");
          window.location.href = "../index.html";
        } else {
          alert("Registration failed. Please try again later.");
        }
      });
  });
  