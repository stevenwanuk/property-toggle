package com.sven.toggle.condition;

public enum PropertyToggle {

    BOOLEAN_TOGGLE("toggle.boolean.enabled", "true"),
    STRING_TOGGLE("toggle.string.enabled", "enabled_string_toggle_value");

    private String propertyName;
    private String propertyValue;

    PropertyToggle(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }

    @Override
    public String toString() {
        return "PropertyToggle{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                '}';
    }
}
