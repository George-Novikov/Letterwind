package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.senders.Sender;
import com.georgen.letterwind.messaging.senders.SenderFactory;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.tools.AnnotationGuard;

import java.util.Set;

public class MessageBroker {
    private static final SenderFactory SENDER_FACTORY = new SenderFactory();

    /**
     * Send a message to every registered topic with the specified (T) message type (i.e. fan-out distribution)
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T message) throws LetterwindException {
        validateMessage(message);
        Class messageType = message.getClass();

        Sender sender = SENDER_FACTORY.getSender(messageType);
        LetterwindControls controls = LetterwindControls.getInstance();

        Set<LetterwindTopic> topics = controls.getAllTopicsWithMessageType(messageType);
        if (topics == null) throw new LetterwindException("No registered topics with the specified message type were found.");

        return sender.send(message, topics);
    }

    /**
     * Send a message to all consumers with the specified (T) message type, associated with the topic with the specified name.
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T message, String topicName) throws LetterwindException {
        validateMessage(message);
        if (topicName == null || topicName.isEmpty()) throw new LetterwindException("Topic name cannot be null");
        Class messageType = message.getClass();

        Sender sender = SENDER_FACTORY.getSender(messageType);
        LetterwindControls controls = LetterwindControls.getInstance();

        LetterwindTopic topic = controls.getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("Topic named %s was not found among registered topics.", topicName));
        if (!topic.isValid()) throw new LetterwindException("LetterwindTopic cannot be null or empty.");

        return sender.send(message, topic);
    }

    /**
     * Send a message to all consumers in the topic that accept the specified (T) message type.
     * The topic will not be registered, so this method should be considered a one-time interaction.
     * */
    public static <T> boolean send(@LetterwindMessage T message, LetterwindTopic topic) throws LetterwindException {
        validateMessage(message);
        if (topic == null || !topic.isValid()) throw new LetterwindException("LetterwindTopic cannot be null or empty.");
        Class messageType = message.getClass();

        Sender sender = SENDER_FACTORY.getSender(messageType);

        return sender.send(message, topic);
    }

    private static <T> void validateMessage(@LetterwindMessage T message) throws LetterwindException {
        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);
        AnnotationGuard.validateMessage(message.getClass());
    }
}
