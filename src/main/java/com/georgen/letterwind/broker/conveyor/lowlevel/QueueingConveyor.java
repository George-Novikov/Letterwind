package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.util.PathBuilder;
import com.georgen.letterwind.util.extractors.ConsumerExtractor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;

public class QueueingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        String message = envelope.getSerializedMessage();
        LetterwindTopic topic = envelope.getTopic();

        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        Configuration configuration = Configuration.getInstance();
        String topicPath = PathBuilder.concatenate(configuration.getExchangePath(), topic.getName());

        for (Class consumer : topic.getConsumers()){
            String consumerPath = PathBuilder.concatenate(topicPath, consumer.getName());
            Set<Method> methods = ConsumerExtractor.extractConsumingMethods(consumer, envelope.getMessageType());
            writeToQueueFiles(message, consumerPath, methods);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void writeToQueueFiles(String message, String consumerPath, Set<Method> methods) throws Exception {
        for (Method method : methods){
            String methodPath = PathBuilder.concatenate(consumerPath, method.getName());
            try (FileOperation fileOperation = new FileOperation(methodPath, true)){
                File file = fileOperation.getFile();
                FileIOManager.append(file, message);
            }
        }
    }

}
