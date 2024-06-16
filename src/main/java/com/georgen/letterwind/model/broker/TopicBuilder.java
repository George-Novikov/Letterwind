package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.broker.storages.ConsumerMethodStorage;

import java.util.Set;

public class TopicBuilder {
    private LetterwindTopic topic;

    public TopicBuilder(){ this.topic = new LetterwindTopic(); }

    public TopicBuilder name(String name){
        this.topic.setName(name);
        return this;
    }

    public TopicBuilder remoteHost(String remoteHost) {
        this.topic.setRemoteHost(remoteHost);
        return this;
    }

    public TopicBuilder remotePort(int remotePort) {
        this.topic.setRemotePort(remotePort);
        return this;
    }

    public TopicBuilder consumers(Class... consumers) throws Exception {
        this.topic.setConsumers(consumers);
        return this;
    }

    public TopicBuilder consumers(Set<Class> consumers) {
        this.topic.setConsumers(consumers);
        return this;
    }

    public TopicBuilder consumer(Class consumerClass) throws Exception {
        this.topic.addConsumer(consumerClass);
        return this;
    }

    public LetterwindTopic build(){ return this.topic; }

    public LetterwindTopic activate() throws Exception { return this.topic.activate(); }
}
