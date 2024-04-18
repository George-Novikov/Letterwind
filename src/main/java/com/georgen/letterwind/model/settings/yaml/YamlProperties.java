package com.georgen.letterwind.model.settings.yaml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.settings.LetterwindProperties;

public class YamlProperties extends LetterwindProperties {
    private LetterwindNode hawthorne;

    public LetterwindNode getHawthorne() {
        return hawthorne;
    }

    public void setHawthorne(LetterwindNode hawthorne) {
        this.hawthorne = hawthorne;
    }

    @Override
    public String getProperty(ConfigProperty property){
        if (hawthorne == null || hawthorne.isEmpty()) return null;
        NamingNode naming = hawthorne.getNaming();

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
        return hawthorne == null || hawthorne.isEmpty();
    }
}
