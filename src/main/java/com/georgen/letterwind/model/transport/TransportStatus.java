package com.georgen.letterwind.model.transport;

public enum TransportStatus {
    OK(0),
    ERROR(-1);

    private int code;

    TransportStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
