package com.georgen.letterwind.model.settings.regular;



import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.settings.LetterwindProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RegularProperties extends LetterwindProperties {
    private Properties properties;

    public RegularProperties(){
        this.properties = new Properties();
    }

    public void load(InputStream inStream) throws IOException {
        this.properties.load(inStream);
    }

    @Override
    public String getProperty(ConfigProperty property) {
        return this.properties.getProperty(property.getName());
    }

    @Override
    public boolean isEmpty() {
        return this.properties.isEmpty();
    }
}
