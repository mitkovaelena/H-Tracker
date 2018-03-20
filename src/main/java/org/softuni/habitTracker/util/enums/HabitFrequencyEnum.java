package org.softuni.habitTracker.util.enums;

public enum HabitFrequencyEnum {
    DAILY("Daily"), EVERY_OTHER_DAY("Every Other Day"), MONDAY_TO_FRIDAY("Monday to Friday"), WEEKLY("Weekly"), MONTHLY("Monthly");

    private String frequencyName;

    private HabitFrequencyEnum(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getFrequencyName() {
        return frequencyName;
    }
}
