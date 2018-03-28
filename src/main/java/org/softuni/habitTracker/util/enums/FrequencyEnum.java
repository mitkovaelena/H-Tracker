package org.softuni.habitTracker.util.enums;

public enum FrequencyEnum {
    DAILY("Daily"), EVERY_OTHER_DAY("Every Other Day"), MONDAY_TO_FRIDAY("Monday to Friday"),
    WEEKLY("Weekly"), MONTHLY("Monthly"), YEARLY("Yearly");

    private String frequencyName;

    private FrequencyEnum(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getFrequencyName() {
        return frequencyName;
    }
}
