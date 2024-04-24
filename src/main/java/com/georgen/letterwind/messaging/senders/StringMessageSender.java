package com.georgen.letterwind.messaging.senders;

import com.georgen.letterwind.api.LetterwindTopic;

import java.util.Set;

public class StringMessageSender implements Sender<String> {
    @Override
    public boolean send(String message, LetterwindTopic topic) {
        return false;
    }

    @Override
    public boolean send(String message, Set<LetterwindTopic> topics) {
        return false;
    }
}
