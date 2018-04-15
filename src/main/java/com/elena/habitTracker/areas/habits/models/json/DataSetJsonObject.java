package com.elena.habitTracker.areas.habits.models.json;

import java.util.List;

public class DataSetJsonObject {
     private String fillColor;
     private String strokeColor;
     private String pointColor;
     private String pointStrokeColor;
     private List<Long> data;
     private Boolean scaleOverride;
     private Integer scaleStepWidth;
     private Integer scaleStartValue;

    public DataSetJsonObject(List<Long> data) {
        this.data = data;
        initializeColors();
        this.scaleOverride = true;
        this.scaleStartValue = 0;
        this.scaleStepWidth = 1;
    }

    private void initializeColors(){
        this.fillColor = "rgba(220,220,220,0.5)";
        this.strokeColor = "rgba(220,220,220,1)";
        this.pointColor = "rgba(220,220,220,1)";
        this.pointStrokeColor = "#fff";
    }

    public List<Long> getData() {
        return data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }
}
