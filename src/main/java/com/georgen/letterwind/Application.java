package com.georgen.letterwind;


import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {

    public static void main(String[] args){
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(3);

            Runnable runnable = () -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    log("Runnable slept for 500 milliseconds.");
                } catch (InterruptedException e){
                    log(e.getMessage());
                }
            };

            AtomicInteger counter = new AtomicInteger();

            Callable<String> callable = () -> {
                TimeUnit.MILLISECONDS.sleep(500);
                log("Callable #{}", counter.getAndIncrement());
                return "Callable task result.";
            };

            List<Callable<String>> callableTasks = new ArrayList<>(){{
                add(callable); add(callable); add(callable);
            }};

            executorService.execute(runnable);

            Future<String> future = executorService.submit(callable);

            String invokeAnyResult = executorService.invokeAny(callableTasks);

            List<Future<String>> futures = executorService.invokeAll(callableTasks);

            log("future result: {}", future.get());
            log("invokeAnyResult: {}", invokeAnyResult);
            for (Future task : futures){
                log("future from list result: {}", task.get());
            }

            executorService.shutdown();

        } catch (Exception e){
            log(e.getMessage());
        }
    }

    private static void log(String line, Object arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line, int arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line, String arg){
        line = line.replace("{}", "%s");
        log(String.format(line, arg));
    }

    private static void log(String line){
        System.out.println(line);
    }
}
