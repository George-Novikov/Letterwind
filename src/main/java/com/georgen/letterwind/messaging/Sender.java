package com.georgen.letterwind.messaging;

import com.georgen.letterwind.api.LetterwindTopic;

public interface Sender<T> {
    boolean send(T message);
    boolean send(T message, String topicName);
    boolean send(T message, LetterwindTopic topic);
}
