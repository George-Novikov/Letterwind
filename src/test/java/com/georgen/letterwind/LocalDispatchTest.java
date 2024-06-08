package com.georgen.letterwind;

import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.TestConstants;
import com.georgen.letterwind.model.consumers.TestConsumer;
import com.georgen.letterwind.model.message.TestMessage;
import com.georgen.letterwind.util.TimeLimiter;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.ClassName.class)
public class LocalDispatchTest {
    @Test
    @Order(1)
    public void testInit(){
        try {
            LetterwindTopic topic = LetterwindTopic.build()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .addConsumer(TestConsumer.class);

            LetterwindControls.set().topic(topic);

            LetterwindTopic fetchedTopic = LetterwindControls.get().topic(TestConstants.TEST_TOPIC_NAME);

            assertNotNull(topic);
            assertNotNull(fetchedTopic);
            assertEquals(topic, fetchedTopic);

        } catch (Exception e){
            fail("[TEST FAILURE]: LocalDispatchTest.testInit();", e);
        }
    }

    @Test
    public void testLocalMessageDispatch(){
        try {
            LetterwindTopic topic = LetterwindTopic.build()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .addConsumer(TestConsumer.class);

            LetterwindControls.set().topic(topic);

            LocalDateTime timeLimit = LocalDateTime.now().plusSeconds(10);

            for (int i = 0; i < TestConstants.SENDS_COUNT; i++){
                TestMessage message = new TestMessage(i, TestConstants.TEST_MESSAGE_VALUE);
                Letterwind.send(message);
            }

            /** TestConsumer will place messages here */
            ResultsStorage storage = ResultsStorage.getInstance();

            /** This is the way to wait for messages to be received by all consumers */
            while (storage.getCounter().get() < TestConstants.SENDS_COUNT){
                TimeLimiter.throwIfExceeds(timeLimit);
            }

            Map<Integer, TestMessage> results = storage.getResults();
            Set<Integer> keys = results.keySet();
            Set<Integer> messageIDs = results.values().stream().map(message -> message.getId()).collect(Collectors.toSet());

            assertTrue(!results.isEmpty());
            assertEquals(TestConstants.SENDS_COUNT, results.size());
            assertEquals(TestConstants.SENDS_COUNT, storage.getCounter().get());
            assertTrue(keys.stream().allMatch(key -> messageIDs.contains(key)));

            storage.clear();

        } catch (Exception e){
            fail("[TEST FAILURE]: LocalDispatchTest.testLocalMessageDispatch();", e);
        }
    }

    private void cleanUp(){

    }
}
