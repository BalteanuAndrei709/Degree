
function logout() {
    // Clear session-related data, such as local storage or cookies
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("role");
    
    // Redirect to the desired page, e.g., index.html
    window.location.href = "../index.html";
}