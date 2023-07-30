document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("search-available-form")
    .addEventListener("submit", async function (event) {
      event.preventDefault();
      let data = await getAvailableDates();
      populateAvaiableDatesTable(data);
    });
});

document.addEventListener("DOMContentLoaded", function () {
  const selectElement = document.getElementById("ensured");
  // Get the selected value when a change event occurs
  selectElement.addEventListener("change", (event) => {
    const selectedValue = event.target.value;
    ensured = selectedValue;
  });
});

async function getAvailableDates() {
  // Get form values
  var dayFrom = document.getElementById("day-from").value;
  var dayTo = document.getElementById("day-to").value;
  var specialization = document.getElementById("Specialization").value;
  var medicId = document.getElementById("medic-name").value;

  const dateFromObject = new Date(dayFrom);
  const dayFromOnly = dateFromObject.getDate();
  const dateToObject = new Date(dayTo);
  const dayToOnly = dateToObject.getDate();

  // Create JSON object to send in the request body
  var requestBody = {
    dayFrom: dayFromOnly,
    dayTo: dayToOnly,
    specializationId: specialization,
  };

  // Add medicId to the request body if selected
  if (medicId) {
    requestBody.medicId = medicId;
  }

  const token = localStorage.getItem("jwtToken");

  const response = await fetch(
    "http://localhost:8080/api/appointment/available",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // Include the JWT token in the Authorization header
      },
      body: JSON.stringify(requestBody), // Convert the request body to JSON string
    }
  );

  const data = await response.json(); // Parse the response body as JSON and extract the data

  return data; // Return the data
}

let selectedHour = 0;
let ensured;

getSelectedInfoAppointment();

function populateAvaiableDatesTable(data) {
  let searchResults = data;
  var tableBody = document
    .getElementById("search-results-table")
    .getElementsByTagName("tbody")[0];
  tableBody.innerHTML = "";
  if (searchResults.length > 0) {
    searchResults.forEach(function (result) {
      var row = document.createElement("tr");
      var dayCell = document.createElement("td");
      dayCell.textContent = result.day;
      row.appendChild(dayCell);

      var hourCell = document.createElement("td");
      if (result.availableHours.length > 1) {
        var hourSelect = document.createElement("select");
        for (var i = 0; i < result.availableHours.length; i++) {
          var hourOption = document.createElement("option");
          hourOption.textContent = result.availableHours[i];
          hourSelect.appendChild(hourOption);
        }
        hourSelect.addEventListener("change", function (event) {
          selectedHour = event.target.value;
          const timeParts = selectedHour.split(":");
          selectedHour = timeParts[0] + ":" + timeParts[1];
        });
        hourCell.appendChild(hourSelect);
      } else {
        hourCell.textContent = result.availableHours[0];
      }

      row.appendChild(hourCell);

      var medicCell = document.createElement("td");
      medicCell.textContent = result.medicName + " " + result.medicSurname;
      medicCell.setAttribute("id", result.medicId);
      row.appendChild(medicCell);
      var specializationCell = document.createElement("td");
      specializationCell.textContent = result.specialization;
      row.appendChild(specializationCell);
      tableBody.appendChild(row);
    });
  } else {
    var row = document.createElement("tr");
    var noResultsCell = document.createElement("td");
    noResultsCell.textContent = "No results found.";
    noResultsCell.colSpan = 4;
    row.appendChild(noResultsCell);
    tableBody.appendChild(row);
  }
}

function getSelectedInfoAppointment() {
  document.addEventListener("DOMContentLoaded", function () {
    const make_appointment_button = document.getElementById("make-appointment");
    make_appointment_button.addEventListener("click", () => {
      const selectedRow = document.querySelector("#search-results-table tr.selected");
      if (!selectedRow) {
        alert("Please select a row to make an appointment.");
        return;
      }
      const selectedMedicId = selectedRow.cells[2].getAttribute('id');
      const selectedDay = selectedRow.cells[0].innerText;
      const selectedHour = selectedRow.cells[1].innerText;
      const selectedSpecialization = selectedRow.cells[3].innerText;
      const ensuredSelect = document.getElementById('ensured');
      const ensured = ensuredSelect.options[ensuredSelect.selectedIndex].value;
      const url = "http://localhost:8080/api/appointment/make";
      const token = localStorage.getItem("jwtToken");
      const appointmentData = {
        day: selectedDay,
        startHour: selectedHour,
        specialization: selectedSpecialization,
        medicId: selectedMedicId,
        ensured: ensured,
      };
      console.log(appointmentData);
      fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(appointmentData),
      })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return window.location.href="../patients/patient-future-appointments.html"
      })
    });

    document
      .getElementById("search-results-table")
      .addEventListener("click", function (event) {
        var target = event.target;
        // Check if the clicked element is a table cell (td)
        if (target && target.nodeName === "TD") {
          // Remove any previously selected rows
          const selectedRows = document.querySelectorAll("#search-results-table tr.selected");
          selectedRows.forEach(row => {
            row.classList.remove("selected");
          });
          // Get the selected row element
          var selectedRow = target.closest("tr");
          selectedRow.classList.add("selected");
        }
      });
  });
}
