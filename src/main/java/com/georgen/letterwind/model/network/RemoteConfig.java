package com.georgen.letterwind.model.network;

import com.georgen.letterwind.tools.Validator;

public class RemoteConfig {
    private String url;
    private int port;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isValid(){
        return Validator.isValid(this.url) && this.port != 0;
    }
}
