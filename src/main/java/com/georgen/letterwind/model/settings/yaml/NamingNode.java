package com.georgen.letterwind.model.settings.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamingNode {
    private String controlFile;
    private String rootDirectory;
    private String entitiesDirectory;

    @JsonProperty("control-file")
    public String getControlFile() {return controlFile; }

    @JsonProperty("control-file")
    public void setControlFile(String controlFile) { this.controlFile = controlFile; }

    @JsonProperty("root-directory")
    public String getRootDirectory() { return rootDirectory; }

    @JsonProperty("root-directory")
    public void setRootDirectory(String rootDirectory) { this.rootDirectory = rootDirectory; }

    @JsonProperty("entities-directory")
    public String getEntitiesDirectory() { return entitiesDirectory; }

    @JsonProperty("entities-directory")
    public void setEntitiesDirectory(String entitiesDirectory) { this.entitiesDirectory = entitiesDirectory; }

    public boolean isEmpty(){
        return this.controlFile == null || this.controlFile.isEmpty()
                || this.rootDirectory == null || this.rootDirectory.isEmpty()
                || this.entitiesDirectory == null || this.entitiesDirectory.isEmpty();
    }
}
