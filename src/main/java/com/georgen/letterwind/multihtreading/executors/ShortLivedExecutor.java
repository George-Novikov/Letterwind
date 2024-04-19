package com.georgen.letterwind.multihtreading.executors;

import com.georgen.letterwind.multihtreading.BacklogManager;
import com.georgen.letterwind.multihtreading.ExecutorBacklog;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShortLivedExecutor implements AutoCloseable {
    private String id;
    private Long terminationTimeout = 5000L;
    private ExecutorService executorService;
    private BacklogManager backlogManager;

    public ShortLivedExecutor(int threadCount, BacklogManager backlogManager){
        this.id = UUID.randomUUID().toString();
        this.executorService = Executors.newFixedThreadPool(threadCount);
        this.backlogManager = backlogManager;
    }

    public ShortLivedExecutor(int threadCount, Long terminationTimeout, BacklogManager backlogManager){
        this.id = UUID.randomUUID().toString();
        this.terminationTimeout = terminationTimeout;
        this.executorService = Executors.newFixedThreadPool(threadCount);
        this.backlogManager = backlogManager;
    }

    @Override
    public void close() throws Exception {
        if (!executorService.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)){
            List<Runnable> tasks = executorService.shutdownNow();
            backlogManager.consume(new ExecutorBacklog(id, tasks));
        }
    }
}