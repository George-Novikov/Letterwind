package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class QueueRetrievingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.isValid()) return;

        LetterwindControls controls = LetterwindControls.getInstance();

        String messageTypeName = envelope.getMessageTypeName();
        Class<T> messageType = controls.getMessageTypeBySimpleName(messageTypeName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", messageTypeName));

        String topicName = envelope.getTopicName();
        LetterwindTopic topic = controls.getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("There is no registered topic with the name %s.", topicName));
        envelope.setTopic(topic);


    }
}
