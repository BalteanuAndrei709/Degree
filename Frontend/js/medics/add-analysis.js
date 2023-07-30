function getAllAnalysis() {
  const url = "http://localhost:8080/api/analysis/all"; // Replace with your backend API endpoint URL

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
      createAnalysis(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
getAllAnalysis();
let all_categories_div;
document.addEventListener("DOMContentLoaded", function () {
  all_categories_div = document.getElementById("all-categories");
});

let selectedAnalysis = [];

function createAnalysis(data) {
  let columns = data.map((item) => {
    return `
    <div class="category">
    <h2><i class="fas fa-flask"></i>${item.categoryName}</h2>
        <div class="category-analysis">
            ${item.allAnalysisName
              .map((analysis) => {
                return `
                        <div class="analysis">
                            <p><i class="fas fa-vial"></i>${analysis}</p>
                            <div class="div-add-button" >
                                <button class="add-button"><i class="fas fa-plus"></i>Add</button>
                            </div>
                        </div>
                    `;
              })
              .join("")}
        </div>
    </div>
`;
  });
  all_categories_div.innerHTML = columns.join("");

  let categoryArray = document.querySelectorAll(".category");

  categoryArray.forEach((category) => {
    category.addEventListener("click", function () {
      let allAnalysisFromCategory = category.children[1].children;

      Array.from(allAnalysisFromCategory).forEach((banana) => {
        banana.classList.toggle("open");
      });
    });
  });

  const addButtonList = document.querySelectorAll(".add-button");

  addButtonList.forEach((addButton) => {
    addButton.addEventListener("click", () => {
      const analysisName =
        addButton.parentNode.parentNode.querySelector("p").textContent;
      selectedAnalysis.push(analysisName);
      

      const recommendedAnalysesList = document.getElementById(
        "recommended-analyses"
      );
      const newListItem = document.createElement("li");
      const newPItem = document.createElement("p");

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
    });
  });
}
