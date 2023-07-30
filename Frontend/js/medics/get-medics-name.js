// Fetch medic names from backend based on selected specialization
// using a GET request

document.addEventListener("DOMContentLoaded", function () {
  bearerToken = localStorage.getItem("jwtToken");
  const specializationSelect = document.getElementById("Specialization");
  const medicNameSelect = document.getElementById("medic-name");
  specializationSelect.addEventListener("change", () => {
    const selectedSpecializationId = specializationSelect.value;
    medicNameSelect.innerHTML =
      '<option value="" selected>Select Medic</option>'; // Clear previous options
    if (selectedSpecializationId !== "") {
      // Make API call to retrieve medics at selected specialization
      fetch(
        `http://localhost:8080/api/medic/allBySpec?specializationId=${selectedSpecializationId}`,
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      )
        .then((response) => response.json())
        .then((medics) => {
          // Populate the medic name dropdown list
          medics.forEach((medic) => {
            const option = document.createElement("option");
            option.value = medic.id; // Medic ID
            option.textContent = medic.name + medic.surname; // Medic Name
            medicNameSelect.appendChild(option);
          });
        })
        .catch((error) => {
          console.error("Failed to fetch medic names:", error);
        });
    } else {
      // Make API call to retrieve all medics
      fetch("http://localhost:8080/api/medic/all", {
        headers: {
          Authorization: `Bearer ${bearerToken}`,
        },
      })
        .then((response) => response.json())
        .then((medics) => {
          console.log(medics);

          // Populate the medic name dropdown list
          medics.forEach((medic) => {
            const option = document.createElement("option");
            option.value = medic.id; // Medic ID
            option.textContent = medic.completeName; // Medic Name
            medicNameSelect.appendChild(option);
          });
        })
        .catch((error) => {
          console.error("Failed to fetch medic names:", error);
        });
    }
  });
});

document.addEventListener("DOMContentLoaded", function () {
  bearerToken = localStorage.getItem("jwtToken");
  const medicNameSelect = document.getElementById("medic-name");
  medicNameSelect.innerHTML = '<option value="" selected>Select Medic</option>'; // Clear previous options
  fetch("http://localhost:8080/api/medic/all", {
    headers: {
      Authorization: `Bearer ${bearerToken}`,
    },
  })
    .then((response) => response.json())
    .then((medics) => {
      // Populate the medic name dropdown list
      medics.forEach((medic) => {
        const option = document.createElement("option");
        option.value = medic.id; // Medic ID
        option.textContent = medic.completeName; // Medic Name
        medicNameSelect.appendChild(option);
      });
    })
    .catch((error) => {
      console.error("Failed to fetch medic names:", error);
    });
});
