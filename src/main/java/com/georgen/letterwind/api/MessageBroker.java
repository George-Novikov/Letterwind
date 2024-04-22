package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

public class MessageBroker {
    /**
     * Send a message to every registered topic with message type of String.
     * This method requires the topics to be registered in advance.
     * */
    public static boolean send(String message){

    }

    /**
     * Send a message to all consumers with message type of String, associated with the topic with the specified name.
     * This method requires the topic to be registered in advance.
     * */
    public static boolean send(String message, String topicName){

    }

    /**
     * Send a message to all consumers in the topic with message type of String.
     * The topic will not be registered, so this method should be considered a one-time interaction.
     * */
    public static boolean send(String message, LetterwindTopic topic){

    }

    /**
     * Send a message to every registered topic with the specified (T) message type
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T objectMessage){

    }

    /**
     * Send a message to all consumers with the specified (T) message type, associated with the topic with the specified name.
     * This method requires the topics to be registered in advance.
     * */
    public static <T> boolean send(@LetterwindMessage T objectMessage, String topicName){

    }

    /**
     * Send a message to all consumers in the topic that accept the specified (T) message type.
     * The topic will not be registered, so this method should be considered a one-time interaction.
     * */
    public static <T> boolean send(@LetterwindMessage T objectMessage, LetterwindTopic topic){

    }
}
