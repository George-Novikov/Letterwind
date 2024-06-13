package com.georgen.letterwind.transport;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.transport.TransportEnvelope;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransportLayer {
    private TransportServer server;
    private TransportClient globalClient;
    private ConcurrentMap<String, TransportClient> topicClients;
    private AtomicBoolean isServerInit;
    private AtomicBoolean isClientInit;

    private TransportLayer(){
        LetterwindControls controls = LetterwindControls.getInstance();
        initGlobalClient(controls);

        this.topicClients = new ConcurrentHashMap<>();
        this.isServerInit = new AtomicBoolean();
        this.isClientInit = new AtomicBoolean();
    }

    public void initServer(LetterwindControls controls){
        if (controls.isServerActive()){
            this.server = new TransportServer(controls.getServerPort());
        }
    }

    public void shutdownServer(){
        if (this.server != null){
            this.server.shutdown();
        }
    }

    public void initGlobalClient(LetterwindControls controls){
        if (controls.hasRemoteListener()){
            this.globalClient = new TransportClient(
                    controls.getRemoteHost(),
                    controls.getRemotePort()
            );
        }
    }

    public void shutdownGlobalClient(){
        if (this.globalClient != null){
            this.globalClient.shutdown();
        }
    }

    public void initTopicClient(LetterwindTopic topic){
        if (topic.hasRemoteListener()){
            this.topicClients.put(topic.getName(), new TransportClient(topic));
        }
    }

    public void initAllTopicClients(LetterwindControls controls){
        controls.getTopics().values()
                .stream()
                .filter(LetterwindTopic::hasRemoteListener)
                .forEach(topic -> {
                    this.topicClients.put(topic.getName(), new TransportClient(topic));
                });
    }

    public void send(TransportEnvelope envelope) throws InterruptedException, LetterwindException {
        TransportClient client = this.topicClients.get(envelope.getTopicName());

        if (client == null) client = this.globalClient;

        if (client == null){
            throw new LetterwindException("The message could not be sent because no TransportClient was found for this topic.");
        }

        client.send(envelope);
    }

    private static class TransportLayerHolder {
        private static final TransportLayer INSTANCE = new TransportLayer();
    }

    public static TransportLayer getInstance(){
        return TransportLayerHolder.INSTANCE;
    }
}
