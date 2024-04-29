package com.georgen.letterwind.messaging.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;

public class ConsumerInvokingConveyor<@LetterwindMessage T> extends MessageConveyor<@LetterwindMessage T> {

    @Override
    public void process(@LetterwindMessage T message, LetterwindTopic topic) throws Exception {

    }
}
