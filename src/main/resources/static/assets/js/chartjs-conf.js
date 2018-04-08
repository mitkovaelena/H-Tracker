function seedData(data) {
    let lineChartData = JSON.parse(data);

    if (isChartEmpty(lineChartData)) {
        let ctx = document.getElementById('line').getContext('2d');
        let image = new Image();
        image.src = "/assets/img/not-enough-data.jpg";

        image.onload = function () {
            ctx.drawImage(image, 0, 0, 400, 300);
        };
        return;
    }
    console.log(lineChartData);

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

function isChartEmpty(data) {
    // Check for non existent datasets
    if (!data || data.length < 1)
        return true;

    // Filter down the datasets to determine if they contain non zero values
    for (let key in data.datasets[0].data) {
        if (data.datasets[0].data[key] != 0) {
            return false;
        }
    }

    return true;
}
