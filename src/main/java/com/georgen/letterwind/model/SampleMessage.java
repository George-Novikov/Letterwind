package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

@LetterwindMessage(errorHandler = SampleErrorHandler.class)
public class SampleMessage {
    private String value;

    public SampleMessage() {}
    public SampleMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SampleMessage{" +
                "value='" + value + '\'' +
                '}';
    }
}
