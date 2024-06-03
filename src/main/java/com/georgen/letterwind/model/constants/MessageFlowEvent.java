package com.georgen.letterwind.model.constants;

public enum MessageFlowEvent {
    DISPATCH,
    RECEPTION,
    REPROCESSING,
    CLEANING,
    SUCCESS,
    ERROR;

    public boolean isFinal(){
        return SUCCESS.equals(this) || ERROR.equals(this);
    }
}
