package com.georgen.letterwind.util;

import com.georgen.letterwind.model.message.GeneralMessage;
import com.georgen.letterwind.model.message.TestMessage;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Disabled
public class ResultsStorage {
    private ConcurrentMap<Integer, GeneralMessage> results;
    private ConcurrentMap<Integer, String> stringResults;
    private ConcurrentLinkedQueue<Exception> errors;
    private volatile AtomicInteger counter;

    private ResultsStorage(){
        this.results = new ConcurrentHashMap<>();
        this.stringResults = new ConcurrentHashMap<>();
        this.errors = new ConcurrentLinkedQueue<>();
        this.counter = new AtomicInteger(0);
    }

    public ConcurrentMap<Integer, GeneralMessage> getResults() {
        return results;
    }

    public ConcurrentMap<Integer, String> getStringResults() {
        return stringResults;
    }

    public ConcurrentLinkedQueue<Exception> getErrors() {
        return errors;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public void add(GeneralMessage message){
        results.put(counter.getAndIncrement(), message);
    }

    public void add(String message){
        stringResults.put(counter.getAndIncrement(), message);
    }

    public void add(Exception error){
        errors.offer(error);
    }

    public void clear(){
        this.results.clear();
        this.stringResults.clear();
        this.counter.set(0);
    }

    private static class ResultsStorageHolder {
        private static final ConcurrentMap<Class, ResultsStorage> STORAGES = new ConcurrentHashMap<>();
    }

    public static ResultsStorage getForClass(Class relatedClass){
        ResultsStorage storage = ResultsStorageHolder.STORAGES.get(relatedClass);

        if (storage == null){
            storage = new ResultsStorage();
            ResultsStorageHolder.STORAGES.put(relatedClass, storage);
        }

        return storage;
    }
}
