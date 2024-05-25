package com.georgen.letterwind.model.config.yaml;

public class LetterwindNode {
    private NamingNode naming;
    public NamingNode getNaming() { return naming; }

    public void setNaming(NamingNode namingNode) { this.naming = namingNode; }
    public boolean isEmpty(){
        return this.naming == null || this.naming.isEmpty();
    }
}
