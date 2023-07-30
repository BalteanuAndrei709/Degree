fetch("http://localhost:8080/api/data/specialization-stats")
  .then((response) => response.json())
  .then((data) => {
    createSpecializationChart(data);
  })
  .catch((error) => console.error(error));

function createSpecializationChart(data) {
  const specializations = data.specializationStatsList.map(
    (d) => d.specialization
  );
  const appointments = data.specializationStatsList.map((d) => d.number);

  const ctx = document.getElementById("specialization-chart").getContext("2d");
  const chart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: specializations,
      datasets: [
        {
          label: "Number of Appointments",
          data: appointments,
          backgroundColor: "rgba(54, 162, 235, 0.2)",
          borderColor: "rgba(54, 162, 235, 1)",
          borderWidth: 1,
        },
      ],
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 1, // Set the step size to 1
          },
          title: {
            display: true,
            text: "Numarul de persoane programate", // Set the label for the y-axis
            font: {
              size: 16, // Set the font size for the y-axis label
            },
          },
        },
        x: {
          title: {
            display: true,
            text: "Varsta", // Set the label for the x-axis
            font: {
              size: 16, // Set the font size for the x-axis label
            },
          },
          ticks: {
            font: {
              size: 14, // Set the font size for the x-axis ticks
            },
          },
        },
      },
      plugins: {
        legend: {
          display: false, // Hide the legend
        },
        title: {
          display: true,
          text: "Age Distribution", // Set the title for the chart
          font: {
            size: 20, // Set the font size for the chart title
          },
        },
        tooltip: {
          backgroundColor: "rgba(0, 0, 0, 0.8)", // Set the background color for the tooltip
          titleFont: {
            size: 16, // Set the font size for the tooltip title
          },
          bodyFont: {
            size: 14, // Set the font size for the tooltip body
          },
        },
      },
      animation: {
        duration: 2000, // Set the duration of the animation in milliseconds
        easing: "easeInOutQuart", // Set the easing function for the animation
      },
    },
  });
}
