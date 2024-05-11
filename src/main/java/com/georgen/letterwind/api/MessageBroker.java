package com.georgen.letterwind.api;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.util.AnnotationGuard;

import java.util.Set;

public class MessageBroker {

    /**
     * Send a message to every registered topic with the specified (T) message type (i.e. fan-out distribution)
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(T message) throws Exception {
        validateMessage(message);
        Class messageType = message.getClass();

        LetterwindControls controls = LetterwindControls.getInstance();
        MessageFlow messageFlow = MessageFlow.getInstance();

        Set<LetterwindTopic> topics = controls.getAllTopicsWithMessageType(messageType);
        if (topics == null) throw new LetterwindException("No registered topics with the specified message type were found.");

        for (LetterwindTopic topic : topics){
            Envelope<T> envelope = new Envelope<>(message, topic);
            messageFlow.startSend(envelope);
        }

        return true;
    }

    /**
     * Send a message to all consumers with the specified (T) message type, associated with the topic with the specified name.
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(T message, String topicName) throws Exception {
        validateMessage(message);
        if (topicName == null || topicName.isEmpty()) throw new LetterwindException("Topic name cannot be null");

        LetterwindControls controls = LetterwindControls.getInstance();

        LetterwindTopic topic = controls.getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("Topic named %s was not found among registered topics.", topicName));
        if (!topic.isValid()) throw new LetterwindException("LetterwindTopic cannot be null or empty.");

        Envelope<T> envelope = new Envelope<>(message, topic);
        MessageFlow.getInstance().startSend(envelope);

        return true;
    }

    /**
     * Send a message to all consumers in the topic that accept the specified (T) message type.
     * The topic will not be registered, so this method should be considered a one-time interaction.
     * */
    public static <T> boolean send(T message, LetterwindTopic topic) throws Exception {
        validateMessage(message);
        if (topic == null || !topic.isValid()) throw new LetterwindException("LetterwindTopic cannot be null or empty.");

        Envelope<T> envelope = new Envelope<>(message, topic);
        MessageFlow.getInstance().startSend(envelope);

        return true;
    }

    private static <T> void validateMessage(T message) throws LetterwindException {
        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);
        if (!(message instanceof String)) AnnotationGuard.validateMessage(message.getClass());
    }
}
