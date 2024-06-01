package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;

import java.util.Map;
import java.util.Set;

/**
 * A helper class to handle the "Builder" behavior of {@link LetterwindControls} via get() method.
 * Example: int sendersLimit = LetterwindControls.get().sendersLimit();
 * */
public class ControlsGetter {
    public int sendersLimit(){ return LetterwindControls.getInstance().getSendersLimit(); }

    public int receiversLimit(){ return LetterwindControls.getInstance().getReceiversLimit(); }

    public int consumersLimit(){ return LetterwindControls.getInstance().getConsumersLimit(); }

    public int eventHandlersLimit(){ return LetterwindControls.getInstance().getEventHandlersLimit(); }

    public boolean isThreadPoolAdaptive(){ return LetterwindControls.getInstance().isThreadPoolAdaptive(); }

    public boolean isServerActive(){ return LetterwindControls.getInstance().isServerActive(); }

    public int serverPort(){ return LetterwindControls.getInstance().getServerPort(); }

    public String remoteHost(){ return LetterwindControls.getInstance().getRemoteHost(); }

    public int remotePort(){ return LetterwindControls.getInstance().getRemotePort(); }

    public Map<String, LetterwindTopic> topics(){ return LetterwindControls.getInstance().getTopics(); }

    public LetterwindTopic topic(String topicName){ return LetterwindControls.getInstance().getTopic(topicName); }

    public Set<LetterwindTopic> topicsWithMessageType(Class messageType){
        return LetterwindControls.getInstance().getAllTopicsWithMessageType(messageType);
    }

    public ErrorHandler errorHandler(){ return LetterwindControls.getInstance().getErrorHandler(); }

    public SuccessHandler successHandler(){ return LetterwindControls.getInstance().getSuccessHandler(); }
}
