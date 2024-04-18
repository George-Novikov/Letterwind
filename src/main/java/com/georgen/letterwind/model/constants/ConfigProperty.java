package com.georgen.letterwind.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("letterwind.naming.control-file", "storage-schema.json"),
    ROOT_PATH("letterwind.naming.root-directory", "hawthorne"),
    ENTITIES_PATH("letterwind.naming.entities-directory", "entities")
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
