package com.georgen.letterwind;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.SampleConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Disabled
public class LocalDispatchTest {
    @Test
    public void testLocalMessageDispatch(){
        try {
            LetterwindTopic topic = LetterwindTopic.build()
                    .setName("Test Topic")
                    .addConsumer(SampleConsumer.class);

            LetterwindControls.set().topic(topic);

            List<String> results = new ArrayList<>();


        } catch (Exception e){
            Assertions.fail("LocalDispatchTest.testLocalMessageDispatch() failed.", e);
        }
    }
}
