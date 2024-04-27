package com.georgen.letterwind.api.annotations;

import com.georgen.letterwind.model.network.RemoteConfig;

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
     * Regulates the number of consumer instances. If set to less than 1, the consumer will stop working.
     * */
    int concurrentInstances() default 1;
    /**
     * Pre-activation will immediately create a number of concurrent copies specified in "concurrentInstances" parameter.
     * If set to false, instantiation will occur on demand.
     * */
    boolean isPreActivated() default false;
    /**
     * Controls the type of multithreading executor used to process consumer operations.
     * If set to true, the UnclosedExecutor will be selected and remain active for an extended period of time.
     * */
    boolean isReused() default false;
    /**
     * Controls the type of multithreading executor used to process consumer operations.
     * If set to a value greater than 0, it overrides the "isReused" parameter and terminates all consumer processes after timeout.
     * Interrupted processes will be reset to the list of unfinished tasks.
     * */
    long terminationMilliseconds() default 0L;
}
