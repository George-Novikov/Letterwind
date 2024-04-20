package com.georgen.letterwind.multihtreading.executors;

import com.georgen.letterwind.multihtreading.BacklogManager;
import com.georgen.letterwind.multihtreading.TaskBacklog;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShortLivedExecutor implements AutoCloseable {
    private String id;
    private Long terminationTimeout = 5000L;
    private ExecutorService executor;
    private BacklogManager backlogManager;

    public ShortLivedExecutor(int threadCount, BacklogManager backlogManager){
        this.id = UUID.randomUUID().toString();
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.backlogManager = backlogManager;
    }

    public ShortLivedExecutor(int threadCount, Long terminationTimeout, BacklogManager backlogManager){
        this.id = UUID.randomUUID().toString();
        this.terminationTimeout = terminationTimeout;
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.backlogManager = backlogManager;
    }

    @Override
    public void close() throws Exception {
        if (!executor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)){
            List<Runnable> tasks = executor.shutdownNow();
            backlogManager.consume(new TaskBacklog(id, tasks));
        }
    }
}
