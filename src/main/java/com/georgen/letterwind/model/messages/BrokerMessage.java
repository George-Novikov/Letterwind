package com.georgen.letterwind.model.messages;

public enum BrokerMessage implements Descriptive {
    NULL_MESSAGE("A message sent to Letterwind cannot be null."),
    INVALID_MESSAGE("The message did not pass validation.");

    private String description;

    BrokerMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
