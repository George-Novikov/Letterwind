package com.georgen.letterwind.settings;


import com.georgen.letterwind.io.FileFactory;
import com.georgen.letterwind.model.exceptions.InitializationException;
import com.georgen.letterwind.model.messages.SystemMessage;
import com.georgen.letterwind.tools.PathBuilder;
import com.georgen.letterwind.tools.Validator;

import java.io.File;

import static com.georgen.letterwind.model.constants.ConfigProperty.*;

public class Configuration {
    private ConfigReader configReader;

    private static class ConfigurationInitializer {
        private static final Configuration INSTANCE = new Configuration();
    }

    public static Configuration getInstance(){
        return ConfigurationInitializer.INSTANCE;
    }

    public String getRootPath() {
        try {
            String rootFolderName = this.configReader.getProperty(ROOT_PATH);
            return Validator.isValid(rootFolderName) ? rootFolderName : ROOT_PATH.getDefaultValue();
        } catch (Exception e){
            return ROOT_PATH.getDefaultValue();
        }
    }

    public String getExchangePath() {
        try {
            String entitiesPath = this.configReader.getProperty(EXCHANGE_PATH);
            boolean isValidPath = Validator.isValid(entitiesPath);
            return getPathRelativeToRoot(isValidPath ? entitiesPath : EXCHANGE_PATH.getDefaultValue());
        } catch (Exception e){
            return EXCHANGE_PATH.getDefaultValue();
        }
    }

    public File getControlFile() {
        try {
            return FileFactory.getInstance().getFile(configReader.getControlFilePath(), true);
        } catch (Exception e){
            throw new InitializationException(SystemMessage.CONTROL_FILE_LOAD_FAIL, e);
        }
    }

    private String getPathRelativeToRoot(String path){
        return PathBuilder.concatenate(getRootPath(), path);
    }
}
