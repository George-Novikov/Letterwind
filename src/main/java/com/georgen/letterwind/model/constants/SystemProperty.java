package com.georgen.letterwind.model.constants;

public enum SystemProperty {
    APPLICATION_PROPERTIES_NAME("application.properties"),
    APPLICATION_YAML_NAME("src/main/resources/application.yaml"),
    APPLICATION_YML_NAME("src/main/resources/application.yml"),

    LETTERWIND_PROPERTIES_NAME("letterwind.properties"),
    LETTERWIND_YAML_NAME("letterwind.yaml"),
    LETTERWIND_YML_NAME("letterwind.yml"),

    ID_COUNTER_NAME("id-counter"),

    QUEUE_FILE_EXTENSION("lwq"),
    BINARY_DATA_EXTENSION("bin");

    private String value;

    SystemProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
