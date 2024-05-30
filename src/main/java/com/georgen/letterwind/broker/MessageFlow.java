package com.georgen.letterwind.broker;

import com.georgen.letterwind.broker.conveyor.ConveyorFactory;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;
import com.georgen.letterwind.multihtreading.ThreadPool;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class MessageFlow {
    private MessageFlow(){}

    /**
     * All message broker operations are routed via this method.
     * Basically this is a decoupled extension of the MessageConveyor (chain of responsibility implementation).
     * This method breaks the process in two phases — dispatch and reception.
     * This approach allows you to configure the number of threads available for each part.
     * Also, it is handy for processing both local and remote calls, errors, and success events.
     */
    public static <T> Future push(Envelope<T> envelope, FlowEvent event){
        MessageConveyor<T> conveyor = ConveyorFactory.createConveyor(event);
        Runnable runnable = getRunnable(conveyor, envelope);
        return ThreadPool.getInstance().startThreadForEvent(runnable, event);
    }

    private static <T> Runnable getRunnable(MessageConveyor<T> conveyor, Envelope<T> envelope){
        return () -> {
            try {
                conveyor.process(envelope);
            } catch (Exception e) {
                envelope.setException(e);
                push(envelope, FlowEvent.ERROR);
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
