package com.georgen.letterwind.messaging.conveyor;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.remote.RemoteSendingConveyor;
import com.georgen.letterwind.messaging.conveyor.local.LocalSendingConveyor;

public class ConveyorFactory {

    public <@LetterwindMessage T> MessageConveyor<@LetterwindMessage T> createConveyor(
            @LetterwindMessage T message,
            LetterwindTopic topic
    ){
        return topic.hasRemoteConfig() ? new RemoteSendingConveyor<>() : new LocalSendingConveyor<>();
    }
}
