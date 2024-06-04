package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;

public class SerializationConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (!envelope.hasMessage()) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        T message = envelope.getMessage();
        boolean isString = message instanceof String;

        if (!isString){
            MessageSerializer<@LetterwindMessage T> serializer = extractSerializer(message);

            if (serializer == null){
                throwFaultySerializerException(envelope.getMessageTypeName());
            }

            String serializedMessage = serializer.serialize(message);
            envelope.setSerializedMessage(serializedMessage);
        } else {
            envelope.setSerializedMessage((String) message);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private MessageSerializer<T> extractSerializer(T message){
        try {
            Class<? extends MessageSerializer> serializerClass = MessageHandlerStorage.getInstance().getSerializer(message.getClass());
            return (MessageSerializer<T>) serializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }

    private void throwFaultySerializerException(String messageTypeName) throws LetterwindException {
        throw new LetterwindException(
                String.format(
                        "The serializer class specified for the %s class is faulty.",
                        messageTypeName
                )
        );
    }
}
