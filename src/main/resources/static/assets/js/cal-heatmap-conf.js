function seedHeatmapData(selector, id) {
    let url = "/users/statistics/" + id;
    selector = "#" + selector;

    $.getJSON(url).done(function (heatmapData) {

        let cal = new CalHeatMap();
        cal.init({
            itemSelector: selector,
            domain: "month",
            subDomain: "day",
            data: heatmapData,
            start: new Date(2018, 0),
            cellSize: 9.5,
            range: 12,
            domainGutter: 4,
            legend: [1, 2, 5, 8, 12],
            displayLegend: false,
        });
    });
}
