package org.softuni.habitTracker.areas.habits.models.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class LineChartJsonObject {
    private List<String> labels;
    private DataSet[] datasets;

    public LineChartJsonObject(List<LocalDate> labels, DataSet[] datasets) {
        this.setLabels(labels);
        this.setDatasets(datasets);
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<LocalDate> labels) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.labels = labels.stream().map(x->x.format(formatter)).collect(Collectors.toList());
    }

    public DataSet[] getDatasets() {
        return datasets;
    }

    public void setDatasets(DataSet[] datasets) {
        this.datasets = datasets;
    }
}
