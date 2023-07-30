// Fetch the data
fetch('http://localhost:8080/api/data/county-stats')
  .then(response => response.json())
  .then(response => {
    // Get the data from the fetch response
    const data = {
      labels: [],
      values: []
    };

    response.numberPerCountyList.forEach(item => {
      data.labels.push(item.county);
      data.values.push(item.number);
    });

    // Create the chart
    const ctx = document.getElementById('county-chart').getContext('2d');
    const chart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: data.labels,
        datasets: [{
          data: data.values,
          backgroundColor: [
            '#FF6384',
            '#36A2EB',
            '#FFCE56',
            '#8E5EA2',
            '#cba9f',
            '#e8c3b9',
            '#c45850',
            '#FF8C00',
            '#00FF00',
            '#0000FF',
            '#FF00FF',
            '#800000',
            '#808000',
            '#008080',
            '#800080',
            '#000080',
            '#008000',
            '#808080',
            '#C0C0C0',
            '#FFD700',
            '#FFA500',
            '#FF4500',
            '#FF1493',
            '#00FFFF',
            '#00CED1',
            '#9400D3',
            '#FF69B4',
            '#00BFFF',
            '#1E90FF',
            '#7B68EE',
            '#00FA9A',
            '#ADFF2F',
            '#228B22',
            '#FFB6C1',
            '#FFC0CB',
            '#FFA07A',
            '#FF7F50',
            '#FF6347',
            '#FF4500',
            '#FFDAB9',
            '#FFE4B5',
            '#EEE8AA',
            '#F0E68C',
            '#BDB76B',
            '#9ACD32',
            '#7FFF00',
            '#32CD32',
            '#00FF7F',
            '#66CDAA',
            '#AFEEEE',
            '#00FFFF',
            '#ADD8E6',
            '#87CEFA',
            '#87CEEB',
            '#191970',
            '#6A5ACD',
            '#483D8B',
            '#FFFAFA',
            '#F0FFF0',
            '#F5FFFA',
            '#F0FFFF',
            '#F0F8FF',
            '#F8F8FF',
            '#F5F5F5',
            '#FFF5EE',
            '#F5F5DC',
            '#FFFFF0',
            '#FAEBD7',
            '#FAF0E6',
            '#FFF0F5',
            '#FFE4E1',
            '#DCDCDC',
            '#D3D3D3',
            '#C0C0C0',
            '#A9A9A9',
            '#808080',
            '#696969',
            '#778899',
            '#708090',
            '#2F4F4F',
            '#000000'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Statistici privind judetele de unde provin pacientii'
        }
      }
    });
  })
  .catch(error => console.error(error));
