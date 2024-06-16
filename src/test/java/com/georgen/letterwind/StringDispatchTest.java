package com.georgen.letterwind;

import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.TestConstants;
import com.georgen.letterwind.model.consumers.StringConsumer;
import com.georgen.letterwind.util.TimeLimiter;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(4)
public class StringDispatchTest {
    @Test
    public void testLocalStringMessageDispatch(){
        try {
            LetterwindTopic topic = LetterwindTopic.create()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .setConsumers(StringConsumer.class)
                    .activate();

            LocalDateTime timeLimit = LocalDateTime.now().plusSeconds(10);

            for (int i = 0; i < TestConstants.SENDS_COUNT; i++){
                Letterwind.send(TestConstants.TEST_MESSAGE_VALUE);
            }

            /** TestConsumer will place messages here */
            ResultsStorage storage = ResultsStorage.getForClass(StringDispatchTest.class);

            /** This is the way to wait for messages to be received by all consumers */
            while (storage.getCounter().get() < TestConstants.SENDS_COUNT){
                TimeLimiter.throwIfExceeds(timeLimit);
            }

            Map<Integer, String> stringResults = storage.getStringResults();

            assertTrue(!stringResults.isEmpty());
            assertEquals(TestConstants.SENDS_COUNT, stringResults.size());
            assertEquals(TestConstants.SENDS_COUNT, storage.getCounter().get());
            assertEquals(TestConstants.TEST_MESSAGE_VALUE, stringResults.get(0));

            storage.clear();
        } catch (Exception e){
            fail("[TEST FAILURE]: StringMessageDispatch.testLocalStringMessageDispatch();", e);
        }
    }
}
