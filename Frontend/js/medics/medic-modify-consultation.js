document.addEventListener("DOMContentLoaded", function () {
    const button = document.querySelector('button[type="submit"]');
    button.addEventListener('click', function() {
        submitConsultationForm(); // generate PDF and submit data
  
      });
  });
  
  async function generateConsultationPdf() {
    // Get form data
    const name = document.getElementById("name").value;
    const age = document.getElementById("age").value;
    const gender = document.getElementById("gender").value;
    const cnp = document.getElementById("cnp").value;
    const county = document.getElementById("county").value;
    const patientEmail = document.getElementById("patientEmail").value;
    const doctor = document.getElementById("doctor").value;
    const medicEmail = document.getElementById("medicEmail").value;
    const specialization = document.getElementById("specialization").value;
    const date = document.getElementById("date").value;
    const time = document.getElementById("time").value;
    const reason = document.getElementById("reason").value;
    const symptoms = document.getElementById("symptoms").value;
    const diagnosis = document.getElementById("diagnosis").value;
    const recomandations = document.getElementById("recomandations").value;
  
    // Define document structure and styles
    const docDefinition = {
      content: [
        { text: "Consultatie:", style: "header" },
        "\n",
        {
          text: `Subsemnatul/Subsemnata ${name}, domiciliate/a in judetul ${county}, 
            cu varsta de ${age}, avand sexul ${gender} si CNP-UL ${cnp}, cu adresa de email ${patientEmail}, a participat in data de ${date}, 
            la ora ${time} la o consultatie la medicul ${doctor}, specializarea ${specialization}, 
            avand adresa de email ${medicEmail}.`,
          style: "body",
        },
        "\n",
        { text: "Motiv programare:", style: "subheader" },
        "\n",
        { text: reason, style: "body" },
        "\n",
        { text: "Simptome:", style: "subheader" },
        "\n",
        { text: symptoms, style: "body" },
        "\n",
        { text: "Diagnostic preventiv:", style: "subheader" },
        "\n",
        { text: diagnosis, style: "body" },
        "\n",
        { text: "Recomandari:", style: "subheader" },
        "\n",
        { text: recomandations, style: "body" },
      ],
      styles: {
        header: {
          fontSize: 18,
          bold: true,
          alignment: "center",
          margin: [0, 0, 0, 10],
          font: "Roboto",
        },
        subheader: {
          fontSize: 14,
          bold: true,
          margin: [0, 10, 0, 5],
          font: "Roboto",
        },
        body: {
          fontSize: 12,
          margin: [0, 0, 0, 10],
          font: "Roboto",
        },
      },
      defaultStyle: {
        font: "Roboto",
      },
      fonts: {
        Roboto: {
          normal: "fonts/Roboto-Regular.ttf",
          bold: "fonts/Roboto-Medium.ttf",
          italics: "fonts/Roboto-Italic.ttf",
          medium: "fontsRoboto-Medium.ttf",
          bolditalics: "fonts/Roboto-MediumItalic.ttf",
        },
      },
    };
  
    const pdfDoc = pdfMake.createPdf(docDefinition);
  
    // Generate the PDF blob
    const blob = await new Promise((resolve, reject) => {
      pdfDoc.getBlob(
        (blob) => {
          resolve(blob);
        },
        (error) => {
          reject(error);
        }
      );
    });
  
    return { blob, pdfDoc };
  }
  
  async function submitConsultationForm() {
    // Generate PDF
    const { blob, pdfDoc } = await generateConsultationPdf();
  
    const file = new File([blob], "consultation.pdf", {
      type: "application/pdf",
    });
  
    // Create a new FormData object and append the File object
    const formData = new FormData();
    formData.append("file", file);
  
    const reason = document.getElementById("reason").value;
    const symptoms = document.getElementById("symptoms").value;
    const diagnosis = document.getElementById("diagnosis").value;
    const recomandations = document.getElementById("recomandations").value;
  
    const jsonData = {
      reason: reason,
      symptoms: symptoms,
      diagnosis: diagnosis,
      recomandations: recomandations,
    };
    formData.append("jsonData", JSON.stringify(jsonData));
  
    // Send POST request
    const appointmentId = localStorage.getItem("appointmentId");
    localStorage.removeItem("appointmentId");
    localStorage.removeItem("patientId");
    const token = localStorage.getItem("jwtToken");
    console.log(appointmentId);
    fetch(`http://localhost:8080/api/appointment/end/${appointmentId}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    })
      .then((response) => {
        if (response.ok) {
          window.location.href = "../../medics/medic-apointments.html";
          console.log("Success");
        } else {
          console.error("PDF submission failed");
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }
  