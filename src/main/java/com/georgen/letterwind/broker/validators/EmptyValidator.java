package com.georgen.letterwind.broker.validators;

public class EmptyValidator<T> implements MessageValidator<T> {
    @Override
    public boolean isValid(T message) throws Exception {
        return true;
    }
}
