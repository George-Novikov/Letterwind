package com.georgen.letterwind.multihtreading;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.constants.FlowEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private ExecutorService senderExecutor;
    private ExecutorService receiverExecutor;
    private ExecutorService consumerExecutor;
    private boolean isInit;

    private ThreadPool(){}

    public Future startThreadForEvent(Runnable runnable, FlowEvent event){
        switch (event){
            case DISPATCH: {
                return startSenderThread(runnable);
            }
            case RECEPTION: {
                return startReceiverThread(runnable);
            }
            case SUCCESS: {
                return startConsumerThread(runnable);
            }
            default: {
                return startConsumerThread(runnable);
            }
        }
    }

    public Future startSenderThread(Runnable runnable){
        return this.senderExecutor.submit(runnable);
    }

    public Future startReceiverThread(Runnable runnable){ return this.receiverExecutor.submit(runnable); }

    public Future startConsumerThread(Runnable runnable){
        return this.consumerExecutor.submit(runnable);
    }

    public boolean isInit() {
        return isInit
                && senderExecutor != null
                && receiverExecutor != null
                && consumerExecutor != null;
    }

    private void init(){
        if (isInit()) return;

        LetterwindControls controls = LetterwindControls.getInstance();
        Configuration config = Configuration.getInstance();

        int sendersThreads = controls.getSendersLimit();
        int receiversLimit = controls.getReceiversLimit();
        int consumersLimit = controls.getConsumersLimit();

        if (sendersThreads < 1) sendersThreads = config.getSendingThreadsLimit();
        if (receiversLimit < 1) receiversLimit = config.getReceivingThreadsLimit();
        if (consumersLimit < 1) consumersLimit = config.getConsumingThreadsLimit();

        if (sendersThreads < 1) sendersThreads = ConfigProperty.SENDING_THREADS.getDefaultIntValue();
        if (receiversLimit < 1) receiversLimit = ConfigProperty.RECEIVING_THREADS.getDefaultIntValue();
        if (consumersLimit < 1) consumersLimit = ConfigProperty.CONSUMING_THREADS.getDefaultIntValue();

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
