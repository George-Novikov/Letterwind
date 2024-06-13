package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.util.Map;

/**
 * A helper class to handle the "Builder" behavior of {@link LetterwindControls} via set() method.
 * Example: LetterwindControls.set().remoteHost("127.0.0.1").remotePort(8080);
 * */
public class ControlsSetter {

    public ControlsSetter sendersLimit(int sendersLimit){
        LetterwindControls.getInstance().setSendersLimit(sendersLimit);
        return this;
    }

    public ControlsSetter receiversLimit(int receiversLimit){
        LetterwindControls.getInstance().setReceiversLimit(receiversLimit);
        return this;
    }

    public ControlsSetter consumersLimit(int consumersLimit){
        LetterwindControls.getInstance().setConsumersLimit(consumersLimit);
        return this;
    }

    public ControlsSetter eventHandlersLimit(int eventHandlersLimit){
        LetterwindControls.getInstance().setEventHandlersLimit(eventHandlersLimit);
        return this;
    }

    public ControlsSetter isThreadPoolAdaptive(boolean isThreadPoolAdaptive){
        LetterwindControls.getInstance().setScaledToSystemCPU(isThreadPoolAdaptive);
        return this;
    }

    public ControlsSetter serverActive(boolean isServerActive) throws LetterwindException {
        LetterwindControls.getInstance().setServerActive(isServerActive);
        return this;
    }

    public ControlsSetter serverPort(int serverPort){
        LetterwindControls.getInstance().setServerPort(serverPort);
        return this;
    }

    public ControlsSetter remoteHost(String remoteHost){
        LetterwindControls.getInstance().setRemoteHost(remoteHost);
        return this;
    }

    public ControlsSetter remotePort(int remotePort){
        LetterwindControls.getInstance().setRemotePort(remotePort);
        return this;
    }

    public ControlsSetter topics(Map<String, LetterwindTopic> topics){
        LetterwindControls.getInstance().setTopics(topics);
        return this;
    }

    public ControlsSetter topic(LetterwindTopic topic) throws Exception {
        LetterwindControls.getInstance().addTopic(topic);
        return this;
    }

    public ControlsSetter removeTopic(String topicName) throws Exception {
        LetterwindControls.getInstance().removeTopic(topicName);
        return this;
    }

    public ControlsSetter errorHandler(ErrorHandler errorHandler){
        LetterwindControls.getInstance().setErrorHandler(errorHandler);
        return this;
    }

    public ControlsSetter successHandler(SuccessHandler successHandler){
        LetterwindControls.getInstance().setSuccessHandler(successHandler);
        return this;
    }
}
