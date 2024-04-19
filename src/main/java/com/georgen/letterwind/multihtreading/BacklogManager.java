package com.georgen.letterwind.multihtreading;

import java.util.ArrayList;
import java.util.List;

public class BacklogManager {
    private List<ExecutorBacklog> backlogs = new ArrayList<>();

    public void consume(ExecutorBacklog backlog){
        backlogs.add(backlog);
    }

    public List<ExecutorBacklog> getBacklogs() {
        return backlogs;
    }
}
