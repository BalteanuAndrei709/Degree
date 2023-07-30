function setPatientId() {
    let appointmentId = localStorage.getItem("appointmentId");
    
    const url = "http://localhost:8080/api/appointment/getPatientId/" + appointmentId; // Replace with your backend API endpoint URL
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
        console.log(data)
        localStorage.setItem("patientId",data)
      })
      .catch((error) => {
        console.error("Error:", error);
      });
}
setPatientId()