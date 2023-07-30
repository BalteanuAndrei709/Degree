document.addEventListener("DOMContentLoaded", function () {
    const apiUrl = "http://localhost:8080/api/medic/info-medic"; // Replace with your API endpoint URL
    const bearerToken = localStorage.getItem("jwtToken"); // Replace with your bearer token
  
    // Get the form elements
    const nameInput = document.getElementById("name");
    const surnameInput = document.getElementById("surname");
    const ageInput = document.getElementById("age");
    const genderInputs = document.getElementsByName("gender");
    const emailInput = document.getElementById("email");
    const licenseId = document.getElementById("licenseId");
    const specialization = document.getElementById("specialization");
  
    // Fetch the user's account information from the API endpoint
    fetch(apiUrl, {
      headers: {
        Authorization: `Bearer ${bearerToken}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data)
        
        // Populate the form with the retrieved data
        nameInput.value = data.name;
        surnameInput.value = data.surname;
        ageInput.value = data.age;
        emailInput.value = data.email;
        specialization.value = data.specialization
        licenseId.value = data.licenseId
  
        // Check the gender radio button that matches the retrieved data
        for (let i = 0; i < genderInputs.length; i++) {
          if (genderInputs[i].value === data.gender) {
            genderInputs[i].checked = true;
            break;
          }
        }
      })
      .catch((error) => console.error(error));
  });
  