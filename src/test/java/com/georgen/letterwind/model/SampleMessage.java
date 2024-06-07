package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import org.junit.jupiter.api.Disabled;

import java.util.List;

@Disabled
@LetterwindMessage
public class SampleMessage {
    private List<String> results;
    private String value;

    public SampleMessage(){

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
