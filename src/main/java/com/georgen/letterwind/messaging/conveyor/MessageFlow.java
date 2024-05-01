package com.georgen.letterwind.messaging.conveyor;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.conveyor.local.LocalSendingConveyor;
import com.georgen.letterwind.model.constants.Operation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageFlow {
    private MessageFlow(){}

    public void inform(String topicName, Operation operation){
        System.out.println(String.format("%s: %s", topicName, operation.name()));
    }

    public <T> void startSend(T message, LetterwindTopic topic) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                new LocalSendingConveyor<>().process(message, topic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.execute(runnable);

        executor.shutdown();
        if (executor.awaitTermination(1000, TimeUnit.MILLISECONDS)){
            executor.shutdownNow();
        }
    }

    private class MessageFlowHolder {
        private static final MessageFlow INSTANCE = new MessageFlow();
    }

    public static MessageFlow getInstance(){
        return MessageFlowHolder.INSTANCE;
    }
}
