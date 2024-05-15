package com.georgen.letterwind.model.transport;

public class ServerConfig {
    private boolean isActive;
    private int port;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isValid(){
        return this.port != 0;
    }
}
