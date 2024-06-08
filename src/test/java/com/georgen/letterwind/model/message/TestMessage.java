package com.georgen.letterwind.model.message;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.TestMessageSerializer;
import org.junit.jupiter.api.Disabled;

import java.time.LocalDateTime;

@Disabled
@LetterwindMessage(serializer = TestMessageSerializer.class)
public class TestMessage {
    private int id;
    private LocalDateTime time;
    private String value;

    public TestMessage(){}

    public TestMessage(int id, String value) {
        this.id = id;
        this.time = LocalDateTime.now();
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TestMessage{" +
                "id=" + id +
                ", time=" + time +
                ", value='" + value + '\'' +
                '}';
    }
}
