package org.softuni.habitTracker.domain.models.binding;

import org.softuni.habitTracker.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class HabitEditDTO {
    private Long id;

    @NotNull
    @NotEmpty(message = Constants.TITLE_NOT_EMPTY)
    private String title;

    @NotNull(message = Constants.FREQUENCY_NOT_EMPTY)
    private String frequency;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotNull(message = Constants.PRIORITY_NOT_EMPTY)
    private String priority;

    public HabitEditDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
