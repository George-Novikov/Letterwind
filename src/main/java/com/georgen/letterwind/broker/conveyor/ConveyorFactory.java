package com.georgen.letterwind.broker.conveyor;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.remote.RemoteSendingConveyor;
import com.georgen.letterwind.broker.conveyor.local.LocalSendingConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class ConveyorFactory {

    public MessageConveyor createSendingConveyor(Envelope envelope){
        LetterwindTopic topic = envelope.getTopic();
        return topic.hasRemoteConfig() ? new RemoteSendingConveyor<>() : new LocalSendingConveyor<>();
    }

    public MessageConveyor createReceivingConveyor(Envelope envelope){
        LetterwindTopic topic = envelope.getTopic();
        return topic.hasRemoteConfig() ? new RemoteSendingConveyor<>() : new LocalSendingConveyor<>();
    }
}
