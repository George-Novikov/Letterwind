package com.georgen.letterwind;

import com.georgen.letterwind.api.Letterwind;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.TestConstants;
import com.georgen.letterwind.model.consumers.TestConsumer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.message.NoAnnotationMessage;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(1)
public class ExceptionsTest {
    @Test
    public void testUnregisteredStringMessage(){
        try {
            LetterwindTopic topic = LetterwindTopic.create()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .addConsumer(TestConsumer.class);

            LetterwindControls.set().topic(topic);

            assertThrows(
                    LetterwindException.class,
                    () -> Letterwind.send(TestConstants.TEST_MESSAGE_VALUE)
            );
        } catch (Exception e){
            fail("[TEST FAILURE]: ExceptionsTest.testUnregisteredStringMessage();", e);
        }
    }

    @Test
    public void testNoAnnotationMessage(){
        try {
            LetterwindTopic topic = LetterwindTopic.create()
                    .setName(TestConstants.TEST_TOPIC_NAME)
                    .addConsumer(TestConsumer.class);

            LetterwindControls.set().topic(topic);

            NoAnnotationMessage noAnnotationMessage = new NoAnnotationMessage();
            noAnnotationMessage.setValue("A message with no @LetterwindAnnotation");

            assertThrows(
                    LetterwindException.class,
                    () -> Letterwind.send(noAnnotationMessage)
            );

        } catch (Exception e){
            fail("[TEST FAILURE]: ExceptionsTest.testNoAnnotationMessage() failed.", e);
        }
    }
}
