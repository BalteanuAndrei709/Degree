document.addEventListener("DOMContentLoaded", function () {
  const apiUrl = "http://localhost:8080/api/patient/update-info-patient"; // Replace with your API endpoint URL
  const bearerToken = localStorage.getItem("jwtToken"); // Replace with your bearer token

  // Get the form element and add a submit event listener

  // Get the form element and add a submit event listener
  const form = document.querySelector("form");
  form.addEventListener("submit", (event) => {
    event.preventDefault(); // Prevent the default form submission behavior

    // Get the form data
    const formData = new FormData(form);
    const patientInfo = {
      name: formData.get("name"),
      surname: formData.get("surname"),
      age: formData.get("age"),
      gender: formData.get("gender"),
      socialId: formData.get("socialId"),
      county: formData.get("county"),
      email: formData.get("email"),
    };
    console.log(patientInfo)

    // Send the patient info to the API endpoint
    fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${bearerToken}`,
      },
      body: JSON.stringify(patientInfo),
    })
      .then((response) => {
        if (response.ok) {
          this.location.reload()
        } else {
          throw new Error("Failed to save patient info");
        }
      })
      .catch((error) => console.error(error));
  });
});
