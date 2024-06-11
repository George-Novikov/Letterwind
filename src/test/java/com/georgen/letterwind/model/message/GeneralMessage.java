package com.georgen.letterwind.model.message;

import java.time.LocalDateTime;

public abstract class GeneralMessage {
    public abstract int getId();

    public abstract void setId(int id);

    public abstract LocalDateTime getTime();

    public abstract void setTime(LocalDateTime time);

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract Class getSourceClass();

    public abstract void setSourceClass(Class sourceClass);
}
