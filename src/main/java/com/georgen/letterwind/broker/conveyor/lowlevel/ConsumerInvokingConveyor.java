package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
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
            invoke(consumerType, message);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void invoke(Class consumerType, T message) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Set<Method> consumingMethods = ConsumerExtractor.extractConsumingMethods(consumerType, message.getClass());
        Object consumerInstance = consumerType.getDeclaredConstructor().newInstance();

        for (Method method : consumingMethods){
            method.invoke(consumerInstance, message);
        }
    }
}
