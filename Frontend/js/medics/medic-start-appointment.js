function startAppointment() {
  
    const appointmentId = localStorage.getItem("appointmentId");
    
    const url = `http://localhost:8080/api/appointment/start/${appointmentId}`; // Replace with your backend API endpoint URL
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
      .catch((error) => {
        console.error("Error:", error);
      });
  }
  
 
  
  startAppointment()