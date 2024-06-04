package com.georgen.letterwind.broker.handlers;


public abstract class ErrorHandler<T> extends FinalEventHandler<T> {
    public static boolean isValid(Class errorHandlerClass){
        return errorHandlerClass != null && !EmptyErrorHandler.class.equals(errorHandlerClass);
    }
}
