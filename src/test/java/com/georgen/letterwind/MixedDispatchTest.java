package com.georgen.letterwind;

import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.consumers.DefaultAnnotationConsumer;
import com.georgen.letterwind.model.message.DefaultAnnotationMessage;
import com.georgen.letterwind.model.message.GeneralMessage;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.TestConstants;
import com.georgen.letterwind.model.consumers.MixedTypeConsumer;
import com.georgen.letterwind.model.consumers.TestConsumer;
import com.georgen.letterwind.model.message.TestMessage;
import com.georgen.letterwind.util.TimeLimiter;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(3)
public class MixedDispatchTest {
    @Test
    public void testCombinedConsumersDispatch(){
        try {
            LetterwindTopic topic = LetterwindTopic.build()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .setConsumers(TestConsumer.class, DefaultAnnotationConsumer.class, MixedTypeConsumer.class);

            LetterwindControls.set().topic(topic);

            LocalDateTime timeLimit = LocalDateTime.now().plusSeconds(30);

            for (int i = 0; i < TestConstants.SENDS_COUNT; i++){
                TestMessage message = new TestMessage(i, TestConstants.TEST_MESSAGE_VALUE, this.getClass());
                DefaultAnnotationMessage defaultAnnotationMessage = new DefaultAnnotationMessage(i, TestConstants.TEST_MESSAGE_VALUE, this.getClass());

                Letterwind.send(message);
                Letterwind.send(defaultAnnotationMessage);

                Letterwind.send(TestConstants.TEST_MESSAGE_VALUE);
            }

            /** TestConsumer will place messages here */
            ResultsStorage storage = ResultsStorage.getForClass(MixedDispatchTest.class);

            /** There are 4 methods that can consume sent messages, so the check number is quadrupled */
            final int QUADRUPLED_SENDS_COUNT = TestConstants.SENDS_COUNT * 4;

            /** This is the way to wait for messages to be received by all consumers */
            while (storage.getCounter().get() < QUADRUPLED_SENDS_COUNT){
                TimeLimiter.throwIfExceeds(timeLimit);
            }

            Map<Integer, GeneralMessage> results = storage.getResults();
            Map<Integer, String> stringResults = storage.getStringResults();

            assertTrue(!results.isEmpty());
            assertTrue(!stringResults.isEmpty());

            assertEquals(QUADRUPLED_SENDS_COUNT, results.size() + stringResults.size());
            assertEquals(QUADRUPLED_SENDS_COUNT, storage.getCounter().get());

            boolean isMatchingTestValue = stringResults.values()
                    .stream()
                    .allMatch(value -> TestConstants.TEST_MESSAGE_VALUE.equals(value));
            assertTrue(isMatchingTestValue);

            storage.clear();
        } catch (Exception e){
            fail("[TEST FAILURE]: MixedDispatchTest.testCombinedConsumersDispatch();", e);
        }
    }
}