package com.georgen.letterwind.messaging.conveyor;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.highlevel.ClientConveyor;
import com.georgen.letterwind.messaging.conveyor.highlevel.LocalConveyor;

import java.util.Set;
import java.util.stream.Collectors;

public class ConveyorFactory {

    public <@LetterwindMessage T> MessageConveyor<@LetterwindMessage T> createConveyor(
            @LetterwindMessage T message,
            LetterwindTopic topic
    ){
        return topic.hasRemoteConfig() ? new ClientConveyor<>() : new LocalConveyor<>();
    }
}
