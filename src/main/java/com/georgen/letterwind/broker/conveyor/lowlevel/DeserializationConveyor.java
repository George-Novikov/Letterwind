package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.broker.serializers.UniversalSerializer;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class DeserializationConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;
        if (!envelope.hasSerializedMessage()) throw new LetterwindException("Failed to retrieve the message from the queue.");

        Class<T> messageType = getMessageType(envelope.getMessageTypeName());
        if (messageType == null) return;

        boolean isString = messageType.equals(String.class);

        if (!isString){
            MessageSerializer<T> serializer = extractSerializer(messageType);
            if (serializer == null) return;
            T message = serializer.deserialize(envelope.getSerializedMessage());
            if (message == null) return;
            envelope.setMessage(message);
        } else {
            envelope.setMessage((T) envelope.getSerializedMessage());
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private Class<T> getMessageType(String messageTypeName) throws LetterwindException {
        Class<T> messageType = LetterwindControls.getInstance().getMessageTypeBySimpleName(messageTypeName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", messageTypeName));
        return messageType;
    }

    private MessageSerializer<T> extractSerializer(Class<T> messageType){
        try {
            Class<? extends MessageSerializer> serializerClass = MessageHandlerStorage.getInstance().getSerializer(messageType);
            if (UniversalSerializer.class.equals(serializerClass)) return new UniversalSerializer<>(messageType);
            return (MessageSerializer<T>) serializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
