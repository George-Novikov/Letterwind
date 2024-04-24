package com.georgen.letterwind.messaging.senders;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;

import java.util.Set;

public class ComplexMessageSender implements Sender<@LetterwindMessage Object>{
    @Override
    public boolean send(@LetterwindMessage Object message, LetterwindTopic topic) {
        return false;
    }

    @Override
    public boolean send(@LetterwindMessage Object message, Set<LetterwindTopic> topics) {
        return false;
    }
}
