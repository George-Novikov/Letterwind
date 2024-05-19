package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.PathBuilder;

public class QueueRetrievingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        LetterwindControls controls = LetterwindControls.getInstance();
        Configuration config = Configuration.getInstance();

        String messageTypeName = envelope.getMessageTypeName();
        String topicName = envelope.getTopicName();

        Class<T> messageType = getMessageType(messageTypeName, controls);
        LetterwindTopic topic = getTopic(topicName, controls);
        envelope.setTopic(topic);

        String messagePath = PathBuilder.concatenate(
                Configuration.getInstance().getExchangePath(),
                topicName,
                messageTypeName
        );

        System.out.println("Message received: " + envelope.getSerializedMessage());
    }

    private Class<T> getMessageType(String messageTypeName, LetterwindControls controls) throws LetterwindException {
        Class<T> messageType = controls.getMessageTypeBySimpleName(messageTypeName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", messageTypeName));
        return messageType;
    }

    private LetterwindTopic getTopic(String topicName, LetterwindControls controls) throws LetterwindException {
        LetterwindTopic topic = controls.getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("There is no registered topic with the name %s.", topicName));
        return topic;
    }
}
