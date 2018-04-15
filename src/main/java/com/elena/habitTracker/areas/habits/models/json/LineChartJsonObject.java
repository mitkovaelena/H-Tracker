package com.elena.habitTracker.areas.habits.models.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class LineChartJsonObject {
    private List<String> labels;
    private DataSetJsonObject[] datasets;

    public LineChartJsonObject(List<LocalDate> labels, DataSetJsonObject[] datasets) {
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

    public DataSetJsonObject[] getDatasets() {
        return datasets;
    }

    public void setDatasets(DataSetJsonObject[] datasets) {
        this.datasets = datasets;
    }
}
