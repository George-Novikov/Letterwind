package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.util.extractors.MessageSerializerExtractor;

public class SerializationConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (!envelope.hasMessage()) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        T message = envelope.getMessage();
        boolean isString = message instanceof String;

        if (!isString){
            MessageSerializer<@LetterwindMessage T> serializer = extractSerializer(message);
            if (serializer == null) throw new LetterwindException("The serializer class specified within the message config is faulty.");

            String serializedMessage = serializer.serialize(message);
            envelope.setSerializedMessage(serializedMessage);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private MessageSerializer<T> extractSerializer(T message){
        try {
            Class serializerClass = MessageSerializerExtractor.extract(message);
            return (MessageSerializer<T>) serializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
