package com.georgen.letterwind.model.config.yaml;

public class LetterwindNode {
    private NamingNode naming;

    private ConcurrencyNode concurrency;

    private ServerNode server;

    private IoNode io;

    public NamingNode getNaming() { return naming; }

    public void setNaming(NamingNode namingNode) { this.naming = namingNode; }

    public ConcurrencyNode getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(ConcurrencyNode concurrency) {
        this.concurrency = concurrency;
    }

    public ServerNode getServer() {
        return server;
    }

    public void setServer(ServerNode server) {
        this.server = server;
    }

    public IoNode getIo() {
        return io;
    }

    public void setIo(IoNode io) {
        this.io = io;
    }

    public boolean isEmpty(){
        return (this.naming == null || this.naming.isEmpty())
                && (this.concurrency == null || this.concurrency.isEmpty())
                && (this.server == null || this.server.isEmpty())
                && (this.io == null || this.io.isEmpty());
    }
}
