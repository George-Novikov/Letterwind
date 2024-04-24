package com.georgen.letterwind.messaging.queues;

import com.georgen.letterwind.api.LetterwindTopic;

public abstract class MessageQueue<T> {
    abstract void collect(T message, LetterwindTopic topic);
}
