document.addEventListener("DOMContentLoaded", function () {
  let analysisSearched = document.getElementById("search-analysis");
  analysisSearched.addEventListener("keydown", (e) => {
    let analysisSearchedName;
    let timeOut = setTimeout(() => {
      analysisSearchedName = e.target.value;
      if (analysisSearchedName) {
        const url =
          "http://localhost:8080/api/analysis/search/" + analysisSearchedName; // Replace with your backend API endpoint URL

        // Create a request object with the URL and headers
        const request = new Request(url, {
          method: "GET",
          mode: "cors",
        });

        // Fetch the request and handle the response
        fetch(request)
          .then((response) => {
            if (!response.ok) {
              throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
          })
          .then((data) => {
            createDivSearched(data.allAnalysisName);
          })
          .catch((error) => {
            console.error("Error:", error);
          });
      }
      else{
        
        const recommendedAnalysesList = document.getElementById(
          "search-results"
        );
        recommendedAnalysesList.replaceChildren()
      }
    }, 500);
  });
});

function createDivSearched(allAnalysisName) {
  let recommendedAnalysis = document.getElementById("search-results");

  let allItems = allAnalysisName.map((item) => {
    return `
        <div class="analysi-result">
        <p>${item}</p>  
        <button class="search-add-button">Add</button>
        </div>
    `;
  });
  recommendedAnalysis.innerHTML = allItems.join("");

  const addButtonList = document.querySelectorAll(".search-add-button");
 

  addButtonList.forEach((addButton) => {
    addButton.addEventListener("click", () => {
      
      const analysisName =
        addButton.parentNode.querySelector("p").textContent;

      
      
      const recommendedAnalysesList = document.getElementById(
        "recommended-analyses"
      );
      const newListItem = document.createElement("li");
      const newPItem = document.createElement("p")
     
      newPItem.textContent = analysisName;
      const deleteButton = document.createElement("button");
      deleteButton.textContent = "Delete";
      deleteButton.addEventListener("click", () => {
        recommendedAnalysesList.removeChild(newListItem);
        const index = selectedAnalysis.indexOf(analysisName);
        if (index > -1) {
          selectedAnalysis.splice(index, 1);
        }
        
      });
      newListItem.appendChild(deleteButton);
      newListItem.appendChild(newPItem);
      recommendedAnalysesList.appendChild(newListItem);

      let analysisSearched = document.getElementById("search-analysis");
      analysisSearched.value = ""

      let searchResult = document.getElementById("search-results");
      searchResult.innerHTML = ""


    });
  });

}
