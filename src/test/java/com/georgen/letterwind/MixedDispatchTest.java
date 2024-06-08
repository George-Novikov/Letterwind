package com.georgen.letterwind;

import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.TestConstants;
import com.georgen.letterwind.model.consumers.MixedTestConsumer;
import com.georgen.letterwind.model.consumers.StringTestConsumer;
import com.georgen.letterwind.model.consumers.TestConsumer;
import com.georgen.letterwind.model.message.TestMessage;
import com.georgen.letterwind.util.TimeLimiter;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.ClassName.class)
public class MixedDispatchTest {
    @Test
    public void testCombinedConsumersDispatch(){
        try {
            LetterwindTopic topic = LetterwindTopic.build()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .setConsumers(TestConsumer.class, StringTestConsumer.class, MixedTestConsumer.class);

            LetterwindControls.set().topic(topic);

            LocalDateTime timeLimit = LocalDateTime.now().plusSeconds(30);

            for (int i = 0; i < TestConstants.SENDS_COUNT; i++){
                TestMessage message = new TestMessage(i, TestConstants.TEST_MESSAGE_VALUE);
                Letterwind.send(message);
                Letterwind.send(TestConstants.TEST_MESSAGE_VALUE);
            }

            /** TestConsumer will place messages here */
            ResultsStorage storage = ResultsStorage.getInstance();

            /** There are 4 methods that can consume sent messages, so the check number is quadrupled */
            final int QUADRUPLED_SENDS_COUNT = TestConstants.SENDS_COUNT * 4;

            /** This is the way to wait for messages to be received by all consumers */
            while (storage.getCounter().get() < QUADRUPLED_SENDS_COUNT){
                TimeLimiter.throwIfExceeds(timeLimit);
            }

            Map<Integer, TestMessage> results = storage.getResults();
            Map<Integer, String> stringResults = storage.getStringResults();

            assertTrue(!results.isEmpty());
            assertTrue(!stringResults.isEmpty());

            assertEquals(QUADRUPLED_SENDS_COUNT, results.size() + stringResults.size());
            assertEquals(QUADRUPLED_SENDS_COUNT, storage.getCounter().get());

            assertEquals(TestConstants.TEST_MESSAGE_VALUE, stringResults.get(0));

            storage.clear();
        } catch (Exception e){
            fail("[TEST FAILURE]: MixedDispatchTest.testCombinedConsumersDispatch();", e);
        }
    }
}
