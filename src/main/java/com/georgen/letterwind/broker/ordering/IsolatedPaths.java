package com.georgen.letterwind.broker.ordering;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IsolatedPaths {
    private ConcurrentMap<String, Path> isolatedPaths;
    private IsolatedPaths(){ this.isolatedPaths = new ConcurrentHashMap<>(); }

    public void isolate(Path path){
        isolatedPaths.put(path.toString(), path);
    }

    public void release(Path path){
        isolatedPaths.remove(path.toString());
    }

    public boolean isIsolated(Path path){
        return isolatedPaths.containsKey(path.toString());
    }

    private static class IsolatedPathsHolder {
        private static final IsolatedPaths INSTANCE = new IsolatedPaths();
    }

    public static IsolatedPaths getInstance(){ return IsolatedPathsHolder.INSTANCE; }
}
