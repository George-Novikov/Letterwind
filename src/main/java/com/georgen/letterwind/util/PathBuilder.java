package com.georgen.letterwind.util;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.model.broker.Envelope;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PathBuilder {

    public static String concatenate(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String concatenate(String... paths){
        if (paths == null || paths.length < 1) return File.separator;
        return Arrays.stream(paths).collect(Collectors.joining(File.separator));
    }

    public static String getExchangePath(String topicName, Class messageType){
        return concatenate(
                Configuration.getInstance().getExchangePath(),
                topicName, messageType.getSimpleName()
        );
    }

    public static String getExchangePath(Envelope envelope){
        return getMessagePath(Configuration.getInstance().getExchangePath(), envelope);
    }

    public static String getBufferPath(Envelope envelope){
        return getMessagePath(Configuration.getInstance().getBufferPath(), envelope);
    }

    public static String getMessagePath(String parentPath, Envelope envelope){
        return PathBuilder.concatenate(parentPath, envelope.getTopicName(), envelope.getMessageTypeName());
    }

    public static String getExchangePath(LetterwindTopic topic, Envelope envelope){
        return getMessagePath(Configuration.getInstance().getExchangePath(), topic, envelope);
    }

    public static String getBufferPath(LetterwindTopic topic, Envelope envelope){
        return getMessagePath(Configuration.getInstance().getBufferPath(), topic, envelope);
    }

    public static String getMessagePath(String parentPath, LetterwindTopic topic, Envelope envelope){
        return PathBuilder.concatenate(parentPath, topic.getName(), envelope.getMessageTypeName());
    }

    public static String formatSeparators(String string){
        try {
            /** Currently only custom paths relative to the root are supported */
            if (string.startsWith("/") || string.startsWith("\\")) string = string.substring(1);
            return string.replace("/", File.separator).replace("\\", File.separator);
        } catch (Exception e){
            return string;
        }
    }
}
