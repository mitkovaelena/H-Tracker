function seedData(data) {
    let lineChartData = JSON.parse(data);
    let maxValue = Math.max(...lineChartData.datasets[0].data);
    new Chart(document.getElementById("line").getContext("2d"))
        .Line(lineChartData, {
            responsive: true,
            scaleOverride: 1,
            scaleSteps: maxValue,
            scaleStartValue: 0,
            scaleStepWidth: 1
        });
}
