package com.georgen.letterwind.model.messages;


public enum ExceptionCaption implements Descriptive {
    INITIALIZATION_EXCEPTION("Letterwind initialization error: %s"),
    LETTERWIND_EXCEPTION("Letterwind error: %s")
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
