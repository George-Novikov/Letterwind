package com.georgen.letterwind.messaging.senders;

import com.georgen.letterwind.api.LetterwindTopic;

public class StringMessageSender implements Sender<String> {
    @Override
    public boolean send(String message) {
        return false;
    }

    @Override
    public boolean send(String message, String topicName) {
        return false;
    }

    @Override
    public boolean send(String message, LetterwindTopic topic) {
        return false;
    }
}
