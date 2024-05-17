package com.georgen.letterwind.transport;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.transport.TransportEnvelope;

import java.util.List;
import java.util.stream.Collectors;

public class TransportLayer {
    private TransportServer server;
    private TransportClient globalClient;
    private List<TransportClient> topicClients;

    private TransportLayer(){
        LetterwindControls controls = LetterwindControls.getInstance();

        if (controls.isServerActive()){
            this.server = new TransportServer(controls.getServerPort());
        }

        if (controls.hasRemoteListener()){
            this.globalClient = new TransportClient(
                    controls.getRemoteHost(),
                    controls.getRemotePort()
            );
        }

        this.topicClients = controls.getTopics().values()
                .stream()
                .filter(topic -> topic.hasRemoteListener())
                .map(topic -> new TransportClient(topic))
                .collect(Collectors.toList());
    }

    public void send(TransportEnvelope envelope) throws InterruptedException, LetterwindException {
        TransportClient client = this.topicClients
                .stream()
                .filter(topicClient -> topicClient.hasTopic(envelope.getTopicName()))
                .findFirst()
                .orElse(this.globalClient);

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
