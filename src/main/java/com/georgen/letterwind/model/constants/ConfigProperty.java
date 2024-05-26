package com.georgen.letterwind.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("letterwind.naming.control-file", "letterwind-controls.json"),
    ROOT_PATH("letterwind.naming.root-directory", "letterwind"),
    EXCHANGE_PATH("letterwind.naming.exchange-directory", "exchange"),
    BUFFER_PATH("letterwind.naming.buffer-directory", "buffer"),
    SENDING_THREADS("letterwind.concurrency.sending-threads", 10),
    RECEIVING_THREADS("letterwind.concurrency.receiving-threads", 10),
    CONSUMING_THREADS("letterwind.concurrency.consuming-threads", 50),
    SERVER_PORT("letterwind.server.port", 17566),
    FAULTY_MESSAGE_READ_ATTEMPTS("letterwind.io.faulty-message-read-attempts", 1000)
    ;

    private String name;
    private String defaultValue;
    private int defaultIntValue;

    ConfigProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    ConfigProperty(String name, int defaultIntValue) {
        this.name = name;
        this.defaultIntValue = defaultIntValue;
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
}
