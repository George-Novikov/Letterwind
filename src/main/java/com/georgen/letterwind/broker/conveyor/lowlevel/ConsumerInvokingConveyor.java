package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;
import com.georgen.letterwind.multihtreading.ThreadPool;
import com.georgen.letterwind.util.extractors.ConsumerExtractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class ConsumerInvokingConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        T message = envelope.getMessage();
        Set<Class> consumerTypes = envelope.getTopic().getConsumers();

        /** Since the method invocation throws an error, the loop is more readable than the Stream API */
        for (Class consumerType : consumerTypes){
            invokeConsumerMethods(consumerType, message, envelope);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void invokeConsumerMethods(Class consumerType, T message, Envelope<T> envelope) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Set<Method> consumingMethods = ConsumerExtractor.extractConsumingMethods(consumerType, message.getClass());
        Object consumerInstance = consumerType.getDeclaredConstructor().newInstance();

        for (Method method : consumingMethods){
//            method.invoke(consumerInstance, message);
            Runnable runnable = getRunnable(method, consumerInstance, message, envelope);
            ThreadPool.getInstance().startConsumerThread(runnable);
        }
    }

    private void invokeDirectly(Method method, Object consumerInstance, T message) throws InvocationTargetException, IllegalAccessException {
        method.invoke(consumerInstance, message);
    }

    private Runnable getRunnable(Method method, Object consumerInstance, T message, Envelope<T> envelope){
        return () -> {
          try {
              method.invoke(consumerInstance, message);
          } catch (Exception e){
              System.out.println("CONSUMER EXCEPTION");
              envelope.setException(e);
              MessageFlow.push(envelope, MessageFlowEvent.ERROR);
          }
        };
    }
}
