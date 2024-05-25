package com.georgen.letterwind.model.config;


import com.georgen.letterwind.model.constants.ConfigProperty;

public abstract class LetterwindProperties {
    public abstract String getProperty(ConfigProperty key);
    public abstract boolean isEmpty();
}
