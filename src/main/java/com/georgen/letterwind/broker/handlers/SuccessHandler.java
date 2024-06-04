package com.georgen.letterwind.broker.handlers;


public abstract class SuccessHandler<T> extends FinalEventHandler<T> {
    public static boolean isValid(Class successHandlerClass){
        return successHandlerClass != null && !EmptySuccessHandler.class.equals(successHandlerClass);
    }
}
