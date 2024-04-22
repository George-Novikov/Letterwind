package com.georgen.letterwind.messaging;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;

public class ComplexMessageSender implements Sender<@LetterwindMessage Object>{
    @Override
    public boolean send(@LetterwindMessage Object message) {
        return false;
    }

    @Override
    public boolean send(@LetterwindMessage Object message, String topicName) {
        return false;
    }

    @Override
    public boolean send(@LetterwindMessage Object message, LetterwindTopic topic) {
        return false;
    }
}
