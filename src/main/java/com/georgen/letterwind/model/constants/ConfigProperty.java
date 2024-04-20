package com.georgen.letterwind.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("letterwind.naming.control-file", "letterwind-controls.json"),
    ROOT_PATH("letterwind.naming.root-directory", "letterwind"),
    EXCHANGE_PATH("letterwind.naming.exchange-directory", "exchange")
    ;

    private String name;
    private String defaultValue;

    ConfigProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName(){
        return this.name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
