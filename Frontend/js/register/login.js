document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("loginForm")
    .addEventListener("submit", function (event) {
      event.preventDefault(); // Prevent form submission

      // Get form data
      var username = document.getElementById("username").value;
      var password = document.getElementById("password").value;

      // Create request body object
      var requestBody = {
        username: username,
        password: password,
      };

      // Send POST request to backend
      var xhr = new XMLHttpRequest();
      xhr.open("POST", "http://localhost:8080/api/login");
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var jwtToken = response.token; // assuming the JWT token is returned in 'token' field
            var role = response.role;
            // Handle successful response from backend
            localStorage.setItem("jwtToken", jwtToken);
            localStorage.setItem("role", role);
            document.getElementById("response").innerHTML = "Login successful!";
            // Redirect to home page
            window.location.href = "/index.html"; // replace with your home page URL
          } else {
            // Handle error response from backend
            document.getElementById("response").innerHTML =
              "Login failed. Please try again.";
          }
        }
      };
      xhr.send(JSON.stringify(requestBody));
    });
});
