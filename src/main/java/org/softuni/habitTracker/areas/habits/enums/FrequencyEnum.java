package org.softuni.habitTracker.areas.habits.enums;

public enum FrequencyEnum {
    DAILY("Daily", 1), EVERY_OTHER_DAY("Every Other Day", 2), MONDAY_TO_FRIDAY("Monday to Friday", 1),
    WEEKLY("Weekly", 7), MONTHLY("Monthly", 1), YEARLY("Yearly", 1);

    private String frequencyName;
    private int interval;

    private FrequencyEnum(String frequencyName, int interval) {
        this.frequencyName = frequencyName;
        this.interval = interval;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public int getInterval() {
        return interval;
    }
}
