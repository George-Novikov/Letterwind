package com.georgen.letterwind.broker;

import com.georgen.letterwind.broker.conveyor.ConveyorFactory;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;
import com.georgen.letterwind.multihtreading.ThreadPool;

import java.util.concurrent.*;

public class MessageFlow {
    private static final ErrorStorage ERROR_STORAGE = new ErrorStorage();

    private MessageFlow(){}

    /**
     * All message broker operations are routed via this method.
     * Basically this is a decoupled extension of the MessageConveyor (chain of responsibility pattern implementation).
     * This allows to configure number of threads available for the sending and receiving parts of the MessageConveyor.
     * Also, it is handy for processing both local and remote calls.
     */
    public static <T> void inform(Envelope<T> envelope, FlowEvent operation){
        switch (operation){
            case DISPATCH: {
                System.out.println(String.format("%s: %s", envelope.getTopicName(), operation.name()));
                break;
            }
        }
    }

    public static <T> void startSend(Envelope<T> envelope) {
        MessageConveyor<T> conveyor = ConveyorFactory.createSendingConveyor(envelope);
        Runnable runnable = getRunnable(conveyor, envelope);
        Future senderFuture = ThreadPool.getInstance().startSenderThread(runnable);
    }

    public <T> void startReceive(Envelope<T> envelope){

    }

    private static <T> Runnable getRunnable(MessageConveyor<T> conveyor, Envelope<T> envelope){
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
