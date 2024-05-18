package com.georgen.letterwind;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.MessageBroker;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.SampleConsumer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.model.SampleMessage;
import com.georgen.letterwind.util.extractors.MessageSerializerExtractor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private static final Class[] CONSUMERS = { SampleConsumer.class };


    public static void main(String[] args){
        try {

            LetterwindTopic topic = new LetterwindTopic("SampleTopic", CONSUMERS);
            topic.setRemoteHost("localhost");
            topic.setRemotePort(8080);

            LetterwindControls.getInstance().registerTopic(topic);

            @LetterwindMessage String message = "How are you?";

            MessageBroker.send(message);

        } catch (Exception e){
            log(e.getMessage());
        }
    }

    private static void testClassSend() throws Exception {
        LetterwindTopic topic = new LetterwindTopic("SampleTopic", CONSUMERS);

        LetterwindControls.getInstance().registerTopic(topic);

        SampleMessage message = new SampleMessage("How are you?");

        MessageBroker.send(message);
    }

    private static void testSerialization() throws LetterwindException, JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SampleMessage message = new SampleMessage();
        message.setValue("How are you?");

        Class serializerClass = MessageSerializerExtractor.extract(message);

        MessageSerializer<SampleMessage> serializer = (MessageSerializer<SampleMessage>) serializerClass.getDeclaredConstructor().newInstance();

        String serializedMessage = serializer.serialize(message);

        log(serializedMessage);

        SampleMessage deserializedMessage = serializer.deserialize(serializedMessage);

        log("deserialized message value: " + deserializedMessage.getValue());

        //MessageBroker.send(message);
    }

    private static void testExecutors() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Runnable runnable = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                log("Runnable slept for 500 milliseconds.");
            } catch (InterruptedException e){
                log(e.getMessage());
            }
        };

        AtomicInteger counter = new AtomicInteger();

        Callable<String> callable = () -> {
            TimeUnit.MILLISECONDS.sleep(500);
            log("Callable #{}", counter.getAndIncrement());
            return "Callable task result.";
        };

        List<Callable<String>> callableTasks = new ArrayList<Callable<String>>(){{
            add(callable); add(callable); add(callable);
        }};

        executorService.execute(runnable);

        Future<String> future = executorService.submit(callable);

        String invokeAnyResult = executorService.invokeAny(callableTasks);

        List<Future<String>> futures = executorService.invokeAll(callableTasks);

        log("future result: {}", future.get());
        log("invokeAnyResult: {}", invokeAnyResult);
        for (Future task : futures){
            log("future from list result: {}", task.get());
        }

        executorService.shutdown();
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
