
function getData(url, callback) {
    fetch(url)
      .then(response => response.json())
      .then(data => {
        // Call the callback function with the retrieved data
        callback(data);
      })
      .catch(error => console.error(error));
  }
  
  function displayNumStats(data) {
    // Get a reference to the HTML elements where you want to display the number of accounts, number of patients, and number of medics
    var numAccountsElement = document.getElementById("numAccounts");
    var numPatientsElement = document.getElementById("numPatients");
    var numMedicsElement = document.getElementById("numMedics");
    
    // Check if the data contains properties for number of accounts, number of patients, and number of medics
    if (data.hasOwnProperty("numAccounts") && data.hasOwnProperty("numPatients") && data.hasOwnProperty("numMedics")) {
      // Extract the numbers from the data object
      var numAccounts = data.numAccounts;
      var numPatients = data.numPatients;
      var numMedics = data.numMedics;
      
      // Update the content of the elements with the extracted numbers
      numAccountsElement.innerHTML =  numAccounts;
      numPatientsElement.innerHTML =  numPatients;
      numMedicsElement.innerHTML =  numMedics;
    } else {
      console.error("Data is missing properties for number of accounts, number of patients, and/or number of medics.");
    }
  }
  