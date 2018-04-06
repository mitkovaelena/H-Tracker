function seedHeadMapData(data, selector) {
    let heatmapData = JSON.parse(data);

    var cal = new CalHeatMap();
    cal.init({
        itemSelector: "#" + selector,
        domain: "year",
        subDomain: "day",
        data: heatmapData,
        start: new Date(2018, 0),
        cellSize: 12,
        range: 1,
        domainGutter: 8,
        legend: [1, 2, 5, 10],
        displayLegend: false,
    });
}
