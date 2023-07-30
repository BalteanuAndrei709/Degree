// Fetch medic names from backend using a GET request
bearerToken = localStorage.getItem("jwtToken");
fetch('http://localhost:8080/api/specialization/all',{
    headers: {
        'Authorization': `Bearer ${bearerToken}`
    }
})
.then(response => response.json())
.then(specializations => {
    const specializationSelect = document.getElementById('Specialization');

    // Populate the dropdown list with specialization names and IDs
    specializations.forEach(specialization => {
        const option = document.createElement('option');
        option.value = specialization.id; // Specialization ID
        option.textContent = specialization.name; // Specialization Name
        specializationSelect.appendChild(option);
    });
})
.catch(error => {
    console.error('Failed to fetch specialization names:', error);
});




