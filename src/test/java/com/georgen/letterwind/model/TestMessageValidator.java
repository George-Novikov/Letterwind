package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.validators.MessageValidator;
import com.georgen.letterwind.model.message.TestMessage;

public class TestMessageValidator implements MessageValidator<TestMessage> {
    @Override
    public boolean isValid(TestMessage message) throws Exception {
        return message != null && message.getValue() != null && !message.getValue().isEmpty();
    }
}
