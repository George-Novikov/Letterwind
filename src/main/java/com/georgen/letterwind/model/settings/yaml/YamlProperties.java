package com.georgen.letterwind.model.settings.yaml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.settings.LetterwindProperties;

public class YamlProperties extends LetterwindProperties {
    private LetterwindNode letterwind;

    public LetterwindNode getLetterwind() {
        return letterwind;
    }

    public void setLetterwind(LetterwindNode letterwind) {
        this.letterwind = letterwind;
    }

    @Override
    public String getProperty(ConfigProperty property){
        if (letterwind == null || letterwind.isEmpty()) return null;
        NamingNode naming = letterwind.getNaming();

        switch (property){
            case CONTROL_FILE_NAME:
                return naming.getControlFile();
            case ROOT_PATH:
                return naming.getRootDirectory();
            case ENTITIES_PATH:
                return naming.getEntitiesDirectory();
            default:
                return null;
        }
    }

    @Override
    @JsonIgnore
    public boolean isEmpty(){
        return letterwind == null || letterwind.isEmpty();
    }
}
