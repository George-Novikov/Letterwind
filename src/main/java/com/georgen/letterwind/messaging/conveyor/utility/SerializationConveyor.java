package com.georgen.letterwind.messaging.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.serializers.MessageSerializer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.tools.extractors.MessageSerializerExtractor;

public class SerializationConveyor<@LetterwindMessage T> extends MessageConveyor<@LetterwindMessage T> {

    @Override
    public void process(@LetterwindMessage T message, LetterwindTopic topic) throws Exception {
        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        @LetterwindMessage String serializedMessage = "";

        if (!(message instanceof String)){
            MessageSerializer<@LetterwindMessage T> serializer = extractSerializer(message);
            if (serializer == null) throw new LetterwindException("The serializer class specified within the message config is faulty.");

            serializedMessage = serializer.serialize(message);
        }

        if (hasConveyor()){
            this.getConveyor().process(serializedMessage, topic);
        }
    }

    private MessageSerializer<@LetterwindMessage T> extractSerializer(@LetterwindMessage T message){
        try {
            Class serializerClass = MessageSerializerExtractor.extract(message);
            return (MessageSerializer<@LetterwindMessage T>) serializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
