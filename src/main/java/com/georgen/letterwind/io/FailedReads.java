package com.georgen.letterwind.io;

import com.georgen.letterwind.model.broker.Envelope;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FailedReads {
    private ConcurrentLinkedQueue<Envelope> queue;
    private FailedReads(){ this.queue = new ConcurrentLinkedQueue<>(); }

    public void add(Envelope envelope){
        this.queue.add(envelope);
    }

    public Envelope poll(){
        return this.queue.poll();
    }

    public boolean hasMore(){
        return !this.queue.isEmpty();
    }

    private static class FailedReadsHolder {
        private static final FailedReads INSTANCE = new FailedReads();
    }

    public static FailedReads getInstance(){
        return FailedReadsHolder.INSTANCE;
    }
}
