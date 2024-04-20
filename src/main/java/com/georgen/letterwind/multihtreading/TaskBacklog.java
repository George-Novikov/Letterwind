package com.georgen.letterwind.multihtreading;

import java.util.List;

public class TaskBacklog {
    private String executorId;
    private List<Runnable> tasks;

    public TaskBacklog(String executorId, List<Runnable> tasks) {
        this.executorId = executorId;
        this.tasks = tasks;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public List<Runnable> getTasks() {
        return tasks;
    }

    public void setTasks(List<Runnable> tasks) {
        this.tasks = tasks;
    }
}
