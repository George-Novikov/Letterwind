package com.georgen.letterwind.util;

import com.georgen.letterwind.model.exceptions.LetterwindException;
import org.junit.jupiter.api.Disabled;

import java.time.LocalDateTime;

@Disabled
public class TimeLimiter {
    public static void throwIfExceeds(LocalDateTime timeLimit) throws LetterwindException {
        if (LocalDateTime.now().isAfter(timeLimit)){
            throw new LetterwindException("The dispatch test is waiting too long.");
        }
    }
}
