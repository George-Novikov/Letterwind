package com.georgen.letterwind.broker;

import com.georgen.letterwind.broker.conveyor.ConveyorFactory;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.ConveyorOperation;
import com.georgen.letterwind.multihtreading.ThreadPool;

import java.util.concurrent.*;

public class MessageFlow {
    private static final ConveyorFactory CONVEYOR_FACTORY = new ConveyorFactory();
    private static final ErrorStorage ERROR_STORAGE = new ErrorStorage();

    private MessageFlow(){}

    public void inform(String topicName, ConveyorOperation operation){
        switch (operation){
            case SEND -> System.out.println(String.format("%s: %s", topicName, operation.name()));
        }
    }

    public <T> void startSend(Envelope<T> envelope) {
        MessageConveyor<T> conveyor = CONVEYOR_FACTORY.createSendingConveyor(envelope);
        Runnable runnable = toRunnable(conveyor, envelope);
        Future senderFuture = ThreadPool.getInstance().startSenderThread(runnable);
    }

    public <T> void startReceive(Envelope<T> envelope){

    }

    private <T> Runnable toRunnable(MessageConveyor<T> conveyor, Envelope<T> envelope){
        return () -> {
            try {
                conveyor.process(envelope);
            } catch (Exception e) {
                ERROR_STORAGE.register(envelope, e);
            }
        };
    }

    private static class MessageFlowHolder {
        private static final MessageFlow INSTANCE = new MessageFlow();
    }

    public static MessageFlow getInstance(){
        return MessageFlowHolder.INSTANCE;
    }
}
