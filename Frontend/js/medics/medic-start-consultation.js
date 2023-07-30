function startAppointment() {
  const appointmentId = localStorage.getItem("appointmentId");

  const url = `http://localhost:8080/api/consultation/start/${appointmentId}`; // Replace with your backend API endpoint URL
  const token = localStorage.getItem("jwtToken"); // Replace with your actual bearer token

  const headers = new Headers();
  headers.append("Authorization", `Bearer ${token}`);

  const request = new Request(url, {
    method: "GET",
    headers: headers,
    mode: "cors",
  });

  fetch(request)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      fillInForm(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

function fillInForm(consultationInfo) {
  console.log(consultationInfo);

  //patient info
  document.getElementById("name").value =
    consultationInfo.patientName + " " + consultationInfo.patientSurname;
  document.getElementById("age").value = consultationInfo.patientAge;
  document.getElementById("gender").value = consultationInfo.patientGender;
  document.getElementById("cnp").value = consultationInfo.patientSocialId;
  document.getElementById("county").value = consultationInfo.patientCounty;
  document.getElementById("patientEmail").value = consultationInfo.patientEmail;

  document.getElementById("doctor").value =
    consultationInfo.medicName + " " + consultationInfo.medicSurname;
  document.getElementById("medicEmail").value = consultationInfo.medicEmail;
  document.getElementById("specialization").value =
    consultationInfo.specialization;

  document.getElementById("date").value = consultationInfo.appointmentDay;
  document.getElementById("time").value = consultationInfo.appointmentStartHour;
  document.getElementById("reason").value = consultationInfo.reason;
  document.getElementById("symptoms").value = consultationInfo.symptoms;
  document.getElementById("diagnosis").value = consultationInfo.initialDiagnosis;
  document.getElementById("recomandations").value = consultationInfo.recommendations;
}

startAppointment();
