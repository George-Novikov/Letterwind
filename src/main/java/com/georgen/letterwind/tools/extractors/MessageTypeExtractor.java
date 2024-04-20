package com.georgen.letterwind.tools.extractors;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.util.HashSet;
import java.util.Set;

public class MessageTypeExtractor {
    public static Set<Class> extract(@LetterwindConsumer Object consumerObject){
        Class javaClass = consumerObject.getClass();

        Set<Class> messageClasses = new HashSet<>();


    }
}
