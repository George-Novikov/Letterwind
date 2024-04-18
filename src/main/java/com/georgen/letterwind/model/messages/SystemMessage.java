package com.georgen.letterwind.model.messages;

public enum SystemMessage implements Descriptive {
    CONTROL_FILE_LOAD_FAIL("Failed to load control file.");

    private String description;

    SystemMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
