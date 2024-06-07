package com.georgen.letterwind.model.config.yaml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NamingNode {
    private String rootDirectory;
    private String exchangeDirectory;
    private String bufferDirectory;

    @JsonProperty("root-directory")
    public String getRootDirectory() {
        return rootDirectory;
    }

    @JsonProperty("root-directory")
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @JsonProperty("exchange-directory")
    public String getExchangeDirectory() {
        return exchangeDirectory;
    }

    @JsonProperty("exchange-directory")
    public void setExchangeDirectory(String exchangeDirectory) {
        this.exchangeDirectory = exchangeDirectory;
    }

    @JsonProperty("buffer-directory")
    public String getBufferDirectory() {
        return bufferDirectory;
    }

    @JsonProperty("buffer-directory")
    public void setBufferDirectory(String bufferDirectory) {
        this.bufferDirectory = bufferDirectory;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return (this.rootDirectory == null || this.rootDirectory.isEmpty())
                && (this.exchangeDirectory == null || this.exchangeDirectory.isEmpty())
                && (this.bufferDirectory == null || this.bufferDirectory.isEmpty());
    }
}
