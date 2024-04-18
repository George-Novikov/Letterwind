package com.georgen.letterwind.model.messages;


public enum ExceptionCaption implements Descriptive {
    INITIALIZATION_EXCEPTION("Hawthorne initialization error: %s"),
    HAWTHORNE_EXCEPTION("Hawthorne error: %s")
    ;

    private String description;

    ExceptionCaption(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
