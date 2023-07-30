document.addEventListener("DOMContentLoaded", function () {
  const button = document.querySelector('button[type="submit"]');
  button.addEventListener("click", function () {
    submitAnalysesForm();
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
      { text: "Formular consultație pacient", style: "header" },
      "\n",
      {
        text: "Date Pacient:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        columns: [
          {
            text: `Nume: ${name}\nCNP: ${cnp}\nVârstă: ${age}\nSex: ${gender}\nJudeț: ${county}\nEmail: ${patientEmail}`,
            width: "auto",
          },
        ],
      },
      "\n",
      {
        text: "Date Medic:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        columns: [
          {
            text: `Nume: ${doctor}\nSpecializare: ${specialization}\nEmail medic: ${medicEmail}`,
            width: "auto",
          },
        ],
      },
      "\n",
      {
        text: "Date Consult:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        text: `Scopul programării: ${reason}\nSimptome: ${symptoms}\nDiagnostic: ${diagnosis}\nRecomandări: ${recomandations}\nData: ${date}\n Ora: ${time}`,
        style: "body",
      },
    ],
    styles: {
      header: {
        fontSize: 24,
        bold: true,
        alignment: "center",
        margin: [0, 0, 0, 20],
        font: "Roboto",
      },
      subheader: {
        fontSize: 18,
        bold: true,
        margin: [0, 20, 0, 10],
        font: "Roboto",
      },
      body: {
        fontSize: 14,
        margin: [0, 0, 0, 20],
        font: "Roboto",
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
        localStorage.removeItem("appointmentId");
        localStorage.removeItem("patientId");
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

async function generateAnalysisPdf(analysesList) {
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

  let analyses = "";

  // Loop over the list and add each element to the text content with a line break
  for (let i = 0; i < analysesList.length; i++) {
    analyses += analysesList[i] + "\n";
  }

  // Define document structure and styles
  const docDefinition = {
    content: [
      { text: "Formular consultație pacient", style: "header" },
      "\n",
      {
        text: "Date Pacient:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        columns: [
          {
            text: `Nume: ${name}\nCNP: ${cnp}\nVârstă: ${age}\nSex: ${gender}\nJudeț: ${county}\nEmail: ${patientEmail}`,
            width: "auto",
          },
        ],
      },
      "\n",
      {
        text: "Date Medic:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        columns: [
          {
            text: `Nume: ${doctor}\nSpecializare: ${specialization}\nEmail medic: ${medicEmail}`,
            width: "auto",
          },
        ],
      },
      "\n",
      {
        text: "Analize cerute:",
        style: "subheader",
        margin: [0, 10, 0, 5],
      },
      {
        text: analyses,
        style: "body",
      },
    ],
    styles: {
      header: {
        fontSize: 24,
        bold: true,
        alignment: "center",
        margin: [0, 0, 0, 20],
        font: "Roboto",
      },
      subheader: {
        fontSize: 18,
        bold: true,
        margin: [0, 20, 0, 10],
        font: "Roboto",
      },
      body: {
        fontSize: 14,
        margin: [0, 0, 0, 20],
        font: "Roboto",
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

async function submitAnalysesForm() {
  const recommendedAnalysesDiv = document.getElementById(
    "recommended-analyses"
  );
  let liElements = recommendedAnalysesDiv.querySelectorAll("li");
  let analysesName = [];
  liElements.forEach((li) => {
    const name = li.children[1];

    analysesName.push(name.textContent);
  });

  const appointmentId = localStorage.getItem("appointmentId");

  const token = localStorage.getItem("jwtToken");

  const { blob, pdfDoc } = await generateAnalysisPdf(analysesName);

  const file = new File([blob], "consultation.pdf", {
    type: "application/pdf",
  });

  const formData = new FormData();
  formData.append("file", file);
  formData.append("appointmentId", appointmentId);
  formData.append("analysesRequest", JSON.stringify(analysesName));
  console.log(appointmentId);
  console.log(JSON.stringify(analysesName));

  const response = await fetch(
    "http://localhost:8080/api/analysis-document/upload/" + appointmentId,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    }
  );

  if (!response.ok) {
    throw new Error("Failed to upload file");
  }

  const responseData = await response.json();
  console.log(responseData);
}
