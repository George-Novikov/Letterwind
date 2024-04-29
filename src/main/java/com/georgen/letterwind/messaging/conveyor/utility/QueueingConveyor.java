package com.georgen.letterwind.messaging.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.settings.Configuration;
import com.georgen.letterwind.tools.PathBuilder;
import com.georgen.letterwind.tools.extractors.ConsumerExtractor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;

public class QueueingConveyor extends MessageConveyor<String> {
    @Override
    public void process(String message, LetterwindTopic topic) throws Exception {
        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        Configuration configuration = Configuration.getInstance();
        String topicPath = PathBuilder.concatenate(configuration.getExchangePath(), topic.getName());

        for (Class consumer : topic.getConsumers()){
            String consumerPath = PathBuilder.concatenate(topicPath, consumer.getName());
            Set<Method> methods = ConsumerExtractor.extractConsumingMethods(consumer);
            writeToQueueFiles(message, consumerPath, methods);
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
