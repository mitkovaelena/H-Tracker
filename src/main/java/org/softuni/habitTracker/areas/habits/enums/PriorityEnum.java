package org.softuni.habitTracker.areas.habits.enums;

public enum PriorityEnum {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");

    private String priorityName;

    private PriorityEnum(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityName() {
        return priorityName;
    }
}
