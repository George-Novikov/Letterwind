package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.senders.Sender;
import com.georgen.letterwind.messaging.senders.SenderFactory;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class MessageBroker {
    private static final SenderFactory SENDER_FACTORY = new SenderFactory();

    /**
     * Send a message to every registered topic with the specified (T) message type
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T message) throws LetterwindException {
        Sender sender = SENDER_FACTORY.getSender(message.getClass());
        LetterwindControls controls = LetterwindControls.getInstance();


        return sender.send(message);
    }

    /**
     * Send a message to all consumers with the specified (T) message type, associated with the topic with the specified name.
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T message, String topicName) throws LetterwindException {
        Sender sender = SENDER_FACTORY.getSender(message.getClass());
        LetterwindControls controls = LetterwindControls.getInstance();
    }

    /**
     * Send a message to all consumers in the topic that accept the specified (T) message type.
     * The topic will not be registered, so this method should be considered a one-time interaction.
     * */
    public static <T> boolean send(@LetterwindMessage T message, LetterwindTopic topic) throws LetterwindException {
        Sender sender = SENDER_FACTORY.getSender(message.getClass());
        LetterwindControls controls = LetterwindControls.getInstance();


    }
}
