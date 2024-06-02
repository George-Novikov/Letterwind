package com.georgen.letterwind.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.TYPE_USE})
public @interface LetterwindConsumer {
    /**
     * If specified, this parameter will bind this consumer to a specific topic, prohibiting cross-topic behavior.
     * */
    String topicName() default "";
    /**
     * Regulates the number of consumer instances.
     * Each @ConsumerMethod in the class is considered as the separate consumer.
     * If set to less than 1, the consumer will stop working.
     * */
    int concurrentInstances() default 1;
}
