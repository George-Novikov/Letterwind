package com.georgen.letterwind.model.config.yaml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.config.LetterwindProperties;

public class YamlProperties extends LetterwindProperties {
    private LetterwindNode letterwind;

    public LetterwindNode getLetterwind() {
        return letterwind;
    }

    public void setLetterwind(LetterwindNode letterwind) {
        this.letterwind = letterwind;
    }

    @Override
    public String getProperty(ConfigProperty property){
        if (letterwind == null || letterwind.isEmpty()) return null;

        NamingNode naming = letterwind.getNaming();
        ConcurrencyNode concurrency = letterwind.getConcurrency();
        ServerNode server = letterwind.getServer();
        IoNode io = letterwind.getIo();

        switch (property){
            case ROOT_PATH:
                return naming != null ? naming.getRootDirectory() : null;
            case EXCHANGE_PATH:
                return naming != null ?  naming.getExchangeDirectory() : null;
            case BUFFER_PATH:
                return naming != null ? naming.getBufferDirectory() : null;
            case SENDING_THREADS:
                return concurrency != null ? concurrency.getSendingThreads() : null;
            case RECEIVING_THREADS:
                return concurrency != null ? concurrency.getReceivingThreads() : null;
            case CONSUMING_THREADS:
                return concurrency != null ? concurrency.getConsumingThreads() : null;
            case EVENT_HANDLING_THREADS:
                return concurrency != null ? concurrency.getEventHandlingThreads() : null;
            case IS_SCALED_TO_CPU:
                return concurrency != null ? concurrency.getScaledToCPU() : null;
            case SERVER_PORT:
                return server != null ? server.getPort() : null;
            case IO_RETRIES_ON_FAULT:
                return io != null ? io.getRetriesOnFault() : null;
            default:
                return null;
        }
    }

    @Override
    @JsonIgnore
    public boolean isEmpty(){
        return letterwind == null || letterwind.isEmpty();
    }
}
