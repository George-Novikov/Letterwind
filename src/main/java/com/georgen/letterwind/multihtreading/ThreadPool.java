package com.georgen.letterwind.multihtreading;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.model.constants.ConfigProperty;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private ExecutorService senderExecutor;
    private ExecutorService receiverExecutor;
    private ExecutorService consumerExecutor;
    private ExecutorService eventExecutor;
    private ExecutorService cleanUpExecutor;
    private boolean isInit;

    private ThreadPool(){}

    public Future startThreadForEvent(Runnable runnable, MessageFlowEvent event){
        switch (event){
            case DISPATCH: {
                return startSenderThread(runnable);
            }
            case RECEPTION: {
                return startReceiverThread(runnable);
            }
            case REPROCESSING: {
                /** Although counterintuitive, this setup provides both maximum performance and stability. */
                return startConsumerThread(runnable);
            }
            case CLEANING: {
                return startCleaningThread(runnable);
            }
            case ERROR: {
                return startEventThread(runnable);
            }
            case SUCCESS: {
                return startEventThread(runnable);
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

    public Future startConsumerThread(Runnable runnable){ return this.consumerExecutor.submit(runnable); }

    public Future startEventThread(Runnable runnable){ return this.eventExecutor.submit(runnable); }

    public Future startCleaningThread(Runnable runnable){ return this.cleanUpExecutor.submit(runnable); }

    public boolean isInit() {
        return isInit
                && senderExecutor != null
                && receiverExecutor != null
                && consumerExecutor != null
                && eventExecutor != null
                && cleanUpExecutor != null;
    }

    private void init(){
        if (isInit()) return;

        LetterwindControls controls = LetterwindControls.getInstance();
        Configuration config = Configuration.getInstance();

        int sendersLimit = controls.getSendersLimit();
        int receiversLimit = controls.getReceiversLimit();
        int consumersLimit = controls.getConsumersLimit();
        int eventHandlersLimit = controls.getEventHandlersLimit();

        if (sendersLimit < 1) sendersLimit = config.getSendingThreadsLimit();
        if (receiversLimit < 1) receiversLimit = config.getReceivingThreadsLimit();
        if (consumersLimit < 1) consumersLimit = config.getConsumingThreadsLimit();
        if (eventHandlersLimit < 1) eventHandlersLimit = config.getEventThreadsLimit();

        if (sendersLimit < 1) sendersLimit = ConfigProperty.SENDING_THREADS.getDefaultIntValue();
        if (receiversLimit < 1) receiversLimit = ConfigProperty.RECEIVING_THREADS.getDefaultIntValue();
        if (consumersLimit < 1) consumersLimit = ConfigProperty.CONSUMING_THREADS.getDefaultIntValue();
        if (eventHandlersLimit < 1) eventHandlersLimit = ConfigProperty.EVENT_HANDLING_THREADS.getDefaultIntValue();

        if (controls.isScaledToSystemCPU()){
            /** Since the message flow is I/O-intensive process, the x2 multiplier is used to properly saturate the CPU */
            double systemThreshold = Runtime.getRuntime().availableProcessors() * 2;
            double threadsSum = sendersLimit + receiversLimit + consumersLimit + eventHandlersLimit;
            double multiplier = threadsSum > systemThreshold ? systemThreshold/threadsSum : 1;

            sendersLimit *= multiplier;
            receiversLimit *= multiplier;
            consumersLimit *= multiplier;
            eventHandlersLimit *= multiplier;

            if (sendersLimit < 1) sendersLimit = 1;
            if (receiversLimit < 1) receiversLimit = 1;
            if (consumersLimit < 1) consumersLimit = 1;
            if (eventHandlersLimit < 1) eventHandlersLimit = 1;
        }

        this.senderExecutor = Executors.newFixedThreadPool(sendersLimit);
        this.receiverExecutor = Executors.newFixedThreadPool(receiversLimit);
        this.consumerExecutor = Executors.newFixedThreadPool(consumersLimit);
        this.eventExecutor = Executors.newFixedThreadPool(eventHandlersLimit);
        this.cleanUpExecutor = Executors.newFixedThreadPool(1);

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
