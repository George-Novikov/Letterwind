package com.georgen.letterwind.model.exceptions;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

public class LetterwindException extends Exception {
    public LetterwindException(String message){
        super(message);
    }
}
