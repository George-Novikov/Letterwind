package com.georgen.letterwind.messaging.queues;

import com.georgen.letterwind.api.LetterwindTopic;

public class LocalQueue<T> extends MessageQueue<T> {
    @Override
    void collect(T message, LetterwindTopic topic) {

    }
}
