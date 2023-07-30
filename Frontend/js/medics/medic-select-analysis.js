function toggleAnalyses(categoryId) {
    var analyses = document.getElementById(categoryId);
    if (analyses.style.display === "none") {
        analyses.style.display = "block";
    } else {
        analyses.style.display = "none";
    }
}

function addAnalysis(analysisName) {
    var recommendedAnalyses = document.getElementById("recommended-analyses");
    var analysisItem = document.createElement("li");
    analysisItem.innerHTML = analysisName;
    
    // create remove button element
    var removeBtn = document.createElement("button");
    removeBtn.innerHTML = "-";
    removeBtn.onclick = function() {
        recommendedAnalyses.removeChild(analysisItem);
    };
    
    // add remove button to analysis item
    analysisItem.appendChild(removeBtn);
    
    recommendedAnalyses.appendChild(analysisItem);
}