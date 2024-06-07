package com.georgen.letterwind.model.config.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IoNode {
    private String retriesOnFault;

    @JsonProperty("retries-on-fault")
    public String getRetriesOnFault() {
        return retriesOnFault;
    }

    @JsonProperty("retries-on-fault")
    public void setRetriesOnFault(String retriesOnFault) {
        this.retriesOnFault = retriesOnFault;
    }

    public boolean isEmpty(){
        return this.retriesOnFault == null || this.retriesOnFault.isEmpty();
    }
}
