fetch('../includes/header.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('header-placeholder').innerHTML = data;
        var jwtToken = localStorage.getItem("jwtToken");
        let role = localStorage.getItem("role")
        if (jwtToken && role ==='ROLE_PATIENT') {
            // Update header elements for logged-in state
            document.getElementById("patient-appointments").style.display = "block";
            document.getElementById("patient-medics").style.display = "block";
            document.getElementById("patient-my-documents").style.display = "block";
            document.getElementById("logout").style.display = "block";
            document.getElementById("contact-us").style.display = "block";
            document.getElementById("patient-account").style.display = "block";
        }
        else if(jwtToken && role ==='ROLE_MEDIC'){
            // Update header elements for logged-out state
            document.getElementById("medic-appointments-nav").style.display = "block";
            document.getElementById("my-patients").style.display = "block";
            document.getElementById("logout").style.display = "block";
            document.getElementById("contact-us").style.display = "block";
            document.getElementById("medic-account").style.display = "block";

        }
        else if(jwtToken && role ==='ROLE_NURSE'){
            document.getElementById("nurse-recoltation").style.display = "block";
            document.getElementById("nurse-results").style.display = "block";
            document.getElementById("logout").style.display = "block";
            document.getElementById("contact-us").style.display = "block";
            document.getElementById("account").style.display = "block";

        
        }
        else if(jwtToken && role ==='ROLE_DATA_ANALYST'){
            document.getElementById("data-age").style.display = "block";
            document.getElementById("data-county").style.display = "block";
            document.getElementById("logout").style.display = "block";
            document.getElementById("contact-us").style.display = "block";
        
        }
        else {
            // Update header elements for logged-out state

            document.getElementById("login").style.display = "block";
            document.getElementById("contact-us").style.display = "block";
            document.getElementById("services").style.display = "block";
            document.getElementById("about-us").style.display = "block";
        }
    });


  