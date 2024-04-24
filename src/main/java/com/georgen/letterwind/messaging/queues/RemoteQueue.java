package com.georgen.letterwind.messaging.queues;

import com.georgen.letterwind.api.LetterwindTopic;

public class RemoteQueue<T> extends MessageQueue<T> {
    @Override
    public void collect(T message, LetterwindTopic topic) {

    }
}
