package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

@LetterwindMessage()
public class SampleMessage {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}