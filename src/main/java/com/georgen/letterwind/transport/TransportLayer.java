package com.georgen.letterwind.transport;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.model.transport.ServerConfig;

public class TransportLayer {
    private TransportServer server;
    private TransportClient client;

    private TransportLayer(){
        LetterwindControls controls = LetterwindControls.getInstance();

        if (controls.isServerActive()){
            this.server = new TransportServer(controls.getServerPort());
        }

        if (controls.hasRemoteListener()){
            this.client = new TransportClient(
                    controls.getRemoteHost(),
                    controls.getRemotePort()
            );
        }
    }

    private static class TransportLayerHolder {
        private static final TransportLayer INSTANCE = new TransportLayer();
    }

    public static TransportLayer getInstance(){
        return TransportLayerHolder.INSTANCE;
    }
}
