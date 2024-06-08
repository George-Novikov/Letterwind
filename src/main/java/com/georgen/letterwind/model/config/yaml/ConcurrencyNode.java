package com.georgen.letterwind.model.config.yaml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcurrencyNode {
    private String sendingThreads;
    private String receivingThreads;
    private String consumingThreads;
    private String eventHandlingThreads;
    private String scaledToSystemCPU;

    @JsonProperty("sending-threads")
    public String getSendingThreads() {
        return sendingThreads;
    }

    @JsonProperty("sending-threads")
    public void setSendingThreads(String sendingThreads) {
        this.sendingThreads = sendingThreads;
    }

    @JsonProperty("receiving-threads")
    public String getReceivingThreads() {
        return receivingThreads;
    }

    @JsonProperty("receiving-threads")
    public void setReceivingThreads(String receivingThreads) {
        this.receivingThreads = receivingThreads;
    }

    @JsonProperty("consuming-threads")
    public String getConsumingThreads() {
        return consumingThreads;
    }

    @JsonProperty("consuming-threads")
    public void setConsumingThreads(String consumingThreads) {
        this.consumingThreads = consumingThreads;
    }

    @JsonProperty("event-handling-threads")
    public String getEventHandlingThreads() {
        return eventHandlingThreads;
    }

    @JsonProperty("event-handling-threads")
    public void setEventHandlingThreads(String eventHandlingThreads) {
        this.eventHandlingThreads = eventHandlingThreads;
    }

    @JsonProperty("is-scaled-to-cpu")
    public String getScaledToCPU() {
        return scaledToSystemCPU;
    }

    @JsonProperty("is-scaled-to-cpu")
    public void setScaledToCPU(String scaledToSystemCPU) {
        this.scaledToSystemCPU = scaledToSystemCPU;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return (this.sendingThreads == null || this.sendingThreads.isEmpty())
                && (this.receivingThreads == null || this.receivingThreads.isEmpty())
                && (this.consumingThreads == null || this.consumingThreads.isEmpty())
                && (this.eventHandlingThreads == null || this.eventHandlingThreads.isEmpty())
                && (this.scaledToSystemCPU == null || this.scaledToSystemCPU.isEmpty());
    }
}
