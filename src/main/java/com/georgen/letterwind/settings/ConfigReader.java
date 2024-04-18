package com.georgen.letterwind.settings;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.constants.SystemProperty;
import com.georgen.letterwind.model.settings.LetterwindProperties;
import com.georgen.letterwind.model.settings.regular.RegularProperties;
import com.georgen.letterwind.model.settings.yaml.YamlProperties;
import com.georgen.letterwind.tools.PathBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ConfigReader {
    private LetterwindProperties properties;
    private boolean isDefaultValue;

    public String getControlFilePath() {
        String rootFolderName = getProperty(ConfigProperty.ROOT_PATH);
        String controlFileName = getProperty(ConfigProperty.CONTROL_FILE_NAME);

        if (rootFolderName == null) rootFolderName = ConfigProperty.ROOT_PATH.getDefaultValue();
        if (controlFileName == null) controlFileName = ConfigProperty.CONTROL_FILE_NAME.getDefaultValue();

        return PathBuilder.concatenate(rootFolderName, controlFileName);
    }

    public String getProperty(ConfigProperty property) {
        if (isDefaultValue) return property.getDefaultValue();

        boolean isInit = initProperties();
        this.isDefaultValue = !isInit;
        if (isDefaultValue) return property.getDefaultValue();

        if (isInit()){
            return this.properties.getProperty(property);
        }

        return null;
    }

    private boolean isInit(){
        return this.properties != null && !this.properties.isEmpty();
    }

    private boolean initProperties(){
        if (!isInit()){
            this.properties = tryLoadFromRootPath();
            if (this.properties == null) this.properties = tryLoadFromClasspath();
            if (this.properties == null) this.properties = tryLoadFromYaml();
        }

        return isInit();
    }

    private LetterwindProperties tryLoadFromClasspath() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream resourceStream = classLoader.getResourceAsStream(SystemProperty.APPLICATION_PROPERTIES_NAME.getValue())){
            if (resourceStream == null) return null;
            RegularProperties regularProperties = new RegularProperties();
            regularProperties.load(resourceStream);
            return regularProperties;
        } catch (Exception e){
            return null;
        }
    }

    private LetterwindProperties tryLoadFromRootPath() {
        try (InputStream stream = new FileInputStream(SystemProperty.HAWTHORNE_PROPERTIES_NAME.getValue())){
            RegularProperties regularProperties = new RegularProperties();
            regularProperties.load(stream);
            return regularProperties;
        } catch (Exception e){
            return null;
        }
    }

    private LetterwindProperties tryLoadFromYaml() {
        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper(yamlFactory);
            mapper.findAndRegisterModules();

            File yamlFile = new File(SystemProperty.HAWTHORNE_YAML_NAME.getValue());
            if (!yamlFile.exists()) yamlFile = new File(SystemProperty.HAWTHORNE_YML_NAME.getValue());
            if (!yamlFile.exists()) yamlFile = new File(SystemProperty.APPLICATION_YAML_NAME.getValue());
            if (!yamlFile.exists()) yamlFile = new File(SystemProperty.APPLICATION_YML_NAME.getValue());
            if (!yamlFile.exists()) return null;

            YamlProperties yamlProperties = mapper.readValue(yamlFile, YamlProperties.class);

            return yamlProperties;
        } catch (Exception e){
            return null;
        }
    }
}
