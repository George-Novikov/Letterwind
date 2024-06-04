package com.georgen.letterwind;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.MessageBroker;
import com.georgen.letterwind.model.SampleConsumer;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.model.SampleMessage;

import java.lang.reflect.InvocationTargetException;

public class Application {
    private static final Class[] CONSUMERS = { SampleConsumer.class };

    public static void main(String[] args){
        try {

            LetterwindTopic topic = LetterwindTopic.build()
                    .setName("SampleTopic")
                    .addConsumer(SampleConsumer.class);

            LetterwindControls.set().topic(topic);

            for (int i = 0; i < 1000; i++){
                SampleMessage message = new SampleMessage();
                message.setValue("How are you? " + i);
                Letterwind.send(message);
            }

        } catch (Exception e){
            log(e.getMessage());
        }
    }

    private static void testClassSend() throws Exception {
        LetterwindTopic topic = new LetterwindTopic("SampleTopic", CONSUMERS);

        LetterwindControls.getInstance().addTopic(topic);

        SampleMessage message = new SampleMessage("How are you?");

        MessageBroker.send(message);
    }

    private static void testSerialization() throws LetterwindException, JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SampleMessage message = new SampleMessage();
        message.setValue("How are you?");

        Class serializerClass = MessageHandlerStorage.getInstance().getSerializer(message.getClass());

        MessageSerializer<SampleMessage> serializer = (MessageSerializer<SampleMessage>) serializerClass.getDeclaredConstructor().newInstance();

        String serializedMessage = serializer.serialize(message);

        log(serializedMessage);

        SampleMessage deserializedMessage = serializer.deserialize(serializedMessage);

        log("deserialized message value: " + deserializedMessage.getValue());
    }

    private static void log(String line, Object arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line, int arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line, String arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line){
        System.out.println(line);
    }
}
