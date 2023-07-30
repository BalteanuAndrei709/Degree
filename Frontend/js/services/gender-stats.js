// Fetch the data
fetch('http://localhost:8080/api/data/gender-stats')
  .then(response => response.json())
  .then(response => {
    // Get the data from the fetch response
    const data = {
      labels: [],
      values: []
    };

    response.numberPerGenderList.forEach(item => {
      data.labels.push(item.gender);
      data.values.push(item.number);
    });

    // Create the chart
    const ctx = document.getElementById('gender-chart').getContext('2d');
    const chart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: data.labels,
        datasets: [{
          data: data.values,
          backgroundColor: [
            '#FF6384',
            '#36A2EB'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Statistici privind genul pacientilor'
        }
      }
    });
  })
  .catch(error => console.error(error));
