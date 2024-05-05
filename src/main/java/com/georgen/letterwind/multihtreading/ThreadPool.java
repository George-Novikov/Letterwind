package com.georgen.letterwind.multihtreading;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.settings.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private ExecutorService senderExecutor;
    private ExecutorService receiverExecutor;
    private ExecutorService consumerExecutor;
    private boolean isInit;

    private ThreadPool(){}

    public boolean isInit() {
        return isInit;
    }

    public Future startSenderThread(Runnable runnable){
        return this.senderExecutor.submit(runnable);
    }

    public Future startReceiverThread(Runnable runnable){
        return this.receiverExecutor.submit(runnable);
    }

    public Future startConsumerThread(Runnable runnable){
        return this.consumerExecutor.submit(runnable);
    }

    private void init(){
        if (isInit) return;

        LetterwindControls controls = LetterwindControls.getInstance();
        Configuration config = Configuration.getInstance();

        int sendersThreads = config.getSendingThreadsLimit();
        int receiversLimit = config.getReceivingThreadsLimit();
        int consumersLimit = controls.getConsumersLimit();
        if (consumersLimit < 1) consumersLimit = config.getConsumingThreadsLimit();

        this.senderExecutor = Executors.newFixedThreadPool(sendersThreads);
        this.receiverExecutor = Executors.newFixedThreadPool(receiversLimit);
        this.consumerExecutor = Executors.newFixedThreadPool(consumersLimit);

        this.isInit = true;
    }

    private static class ThreadPoolHolder {
        private static final ThreadPool INSTANCE = new ThreadPool();
    }

    public static ThreadPool getInstance(){
        ThreadPool instance = ThreadPoolHolder.INSTANCE;

        if (!instance.isInit()){
            instance.init();
        }

        return instance;
    }
}
