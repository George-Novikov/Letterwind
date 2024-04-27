package com.georgen.letterwind.model.exceptions;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.messages.Descriptive;

public class LetterwindException extends Exception {
    public LetterwindException(String message){
        super(message);
    }

    public LetterwindException(Descriptive descriptive){
        super(descriptive.getDescription());
    }
}
