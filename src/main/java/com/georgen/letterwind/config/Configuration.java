package com.georgen.letterwind.config;


import com.georgen.letterwind.io.FileFactory;
import com.georgen.letterwind.model.exceptions.InitializationException;
import com.georgen.letterwind.model.messages.SystemMessage;
import com.georgen.letterwind.util.PathBuilder;
import com.georgen.letterwind.util.Validator;

import java.io.File;

import static com.georgen.letterwind.model.constants.ConfigProperty.*;

public class Configuration {
    private ConfigReader configReader;

    private Configuration(){
        this.configReader = new ConfigReader();
    }

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
            String exchangePath = this.configReader.getProperty(EXCHANGE_PATH);
            boolean isValidPath = Validator.isValid(exchangePath);
            return getPathRelativeToRoot(isValidPath ? exchangePath : EXCHANGE_PATH.getDefaultValue());
        } catch (Exception e){
            return EXCHANGE_PATH.getDefaultValue();
        }
    }

    public String getBufferPath() {
        try {
            String bufferPath = this.configReader.getProperty(BUFFER_PATH);
            boolean isValidPath = Validator.isValid(bufferPath);
            return getPathRelativeToRoot(isValidPath ? bufferPath : BUFFER_PATH.getDefaultValue());
        } catch (Exception e){
            return BUFFER_PATH.getDefaultValue();
        }
    }

    public int getSendingThreadsLimit(){
        try {
            String sendingThreadsString = this.configReader.getProperty(SENDING_THREADS);
            boolean isValidParam = Validator.isValid(sendingThreadsString);
            return isValidParam ? Integer.valueOf(sendingThreadsString) : SENDING_THREADS.getDefaultIntValue();
        } catch (Exception e){
            return SENDING_THREADS.getDefaultIntValue();
        }
    }

    public int getReceivingThreadsLimit(){
        try {
            String receivingThreadsString = this.configReader.getProperty(RECEIVING_THREADS);
            boolean isValidParam = Validator.isValid(receivingThreadsString);
            return isValidParam ? Integer.valueOf(receivingThreadsString) : RECEIVING_THREADS.getDefaultIntValue();
        } catch (Exception e){
            return RECEIVING_THREADS.getDefaultIntValue();
        }
    }

    public int getConsumingThreadsLimit(){
        try {
            String consumingThreadsString = this.configReader.getProperty(CONSUMING_THREADS);
            boolean isValidParam = Validator.isValid(consumingThreadsString);
            return isValidParam ? Integer.valueOf(consumingThreadsString) : CONSUMING_THREADS.getDefaultIntValue();
        } catch (Exception e){
            return CONSUMING_THREADS.getDefaultIntValue();
        }
    }

    public int getEventThreadsLimit(){
        try {
            String eventThreadsString = this.configReader.getProperty(EVENT_HANDLING_THREADS);
            boolean isValidParam = Validator.isValid(eventThreadsString);
            return isValidParam ? Integer.valueOf(eventThreadsString) : EVENT_HANDLING_THREADS.getDefaultIntValue();
        } catch (Exception e){
            return EVENT_HANDLING_THREADS.getDefaultIntValue();
        }
    }

    public boolean isThreadPoolAdaptive(){
        try {
            String isAdaptiveString = this.configReader.getProperty(EVENT_HANDLING_THREADS);
            boolean isValidParam = Validator.isValid(isAdaptiveString);
            return isValidParam ? Boolean.valueOf(isAdaptiveString) : IS_THREAD_POOL_ADAPTIVE.getDefaultBooleanValue();
        } catch (Exception e){
            return IS_THREAD_POOL_ADAPTIVE.getDefaultBooleanValue();
        }
    }

    public int getIORetriesOnFault(){
        try {
            String faultyMessageReadAttempts = this.configReader.getProperty(IO_RETRIES_ON_FAULT);
            boolean isValidParam = Validator.isValid(faultyMessageReadAttempts);
            return isValidParam ? Integer.valueOf(faultyMessageReadAttempts) : IO_RETRIES_ON_FAULT.getDefaultIntValue();
        } catch (Exception e){
            return IO_RETRIES_ON_FAULT.getDefaultIntValue();
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
