package com.georgen.letterwind.multihtreading.executors;

import java.util.concurrent.ExecutorService;

public class LongLivedExecutor implements AutoCloseable {
    private String id;
    private ExecutorService executor;

    @Override
    public void close() throws Exception {
        if (!executor.isShutdown()) executor.shutdown();
    }
}
