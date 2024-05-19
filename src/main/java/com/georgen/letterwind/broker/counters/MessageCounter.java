package com.georgen.letterwind.broker.counters;

import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.io.FileFactory;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

public class MessageCounter {
    private MessageCounter(){}

    public static void initForAllTopics(Class messageType, Set<String> topicNames) throws IOException {
        for (String topicName : topicNames){
            String messageTypePath = PathBuilder.concatenate(
                    Configuration.getInstance().getExchangePath(),
                    topicName, messageType.getSimpleName()
            );

            AtomicLong counter = MessageCounterHolder.COUNTERS.get(messageTypePath);
            if (counter != null) return;

            initCounter(messageTypePath);
        }
    }

    public static long countForPath(String messageTypePath) throws IOException, LetterwindException {
        AtomicLong counter = MessageCounterHolder.COUNTERS.get(messageTypePath);
        if (counter != null) return counter.incrementAndGet();
        counter = initCounter(messageTypePath);
        return counter.incrementAndGet();
    }

    private static AtomicLong initCounter(String messageTypePath) throws IOException {
        File messageDirectory = FileFactory.getInstance().getFile(messageTypePath, false);
        if (!messageDirectory.exists()) messageDirectory.mkdirs();

        AtomicLong counter = getInitialCounter(messageDirectory.toPath());
        MessageCounterHolder.COUNTERS.put(messageTypePath, counter);
        return counter;
    }

    private static AtomicLong getInitialCounter(Path messageTypePath) throws IOException {
        AtomicLong counter = new AtomicLong();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(messageTypePath)){
            long count = StreamSupport.stream(directoryStream.spliterator(), false).count();
            counter.set(count);
        }
        return counter;
    }

    private static class MessageCounterHolder {
        private static final MessageCounter INSTANCE = new MessageCounter();
        private static final ConcurrentMap<String, AtomicLong> COUNTERS = new ConcurrentHashMap<>();
    }

    public static MessageCounter getInstance(){
        return MessageCounterHolder.INSTANCE;
    }
}
