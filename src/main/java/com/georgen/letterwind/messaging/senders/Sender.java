package com.georgen.letterwind.messaging.senders;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.queues.QueueFactory;

import java.util.Set;

public interface Sender<T> {
    QueueFactory QUEUE_FACTORY = new QueueFactory();
    boolean send(T message, LetterwindTopic topic);
    boolean send(T message, Set<LetterwindTopic> topics);
}
