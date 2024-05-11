package com.georgen.letterwind.model.transport;

import com.georgen.letterwind.util.Validator;

public class RemoteServerConfig {
    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isValid(){
        return Validator.isValid(this.ip) && this.port != 0;
    }
}
