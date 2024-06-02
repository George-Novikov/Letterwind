package com.georgen.letterwind.model.constants;

public enum ConfigProperty {
    ROOT_PATH("letterwind.naming.root-directory", "letterwind"),
    EXCHANGE_PATH("letterwind.naming.exchange-directory", "exchange"),
    BUFFER_PATH("letterwind.naming.buffer-directory", "buffer"),
    SENDING_THREADS("letterwind.concurrency.sending-threads", 10),
    RECEIVING_THREADS("letterwind.concurrency.receiving-threads", 10),
    CONSUMING_THREADS("letterwind.concurrency.consuming-threads", 40),
    EVENT_HANDLING_THREADS("letterwind.concurrency.event-handling-threads", 10),
    IS_THREAD_POOL_ADAPTIVE("letterwind.concurrency.is-adaptive", true),

    SERVER_PORT("letterwind.server.port", 17566),
    IO_RETRIES_ON_FAULT("letterwind.io.retries-on-fault", 10)
    ;

    private String name;
    private String defaultValue;
    private int defaultIntValue;
    private boolean defaultBooleanValue;

    ConfigProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    ConfigProperty(String name, int defaultIntValue) {
        this.name = name;
        this.defaultIntValue = defaultIntValue;
    }

    ConfigProperty(String name, boolean defaultBooleanValue) {
        this.name = name;
        this.defaultBooleanValue = defaultBooleanValue;
    }

    public String getName(){
        return this.name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int getDefaultIntValue() {
        return defaultIntValue;
    }

    public boolean getDefaultBooleanValue() { return defaultBooleanValue; }
}
