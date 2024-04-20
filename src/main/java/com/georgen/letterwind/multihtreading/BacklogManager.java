package com.georgen.letterwind.multihtreading;

import java.util.ArrayList;
import java.util.List;

public class BacklogManager {
    private List<TaskBacklog> backlogs = new ArrayList<>();

    public void consume(TaskBacklog backlog){
        backlogs.add(backlog);
    }

    public List<TaskBacklog> getBacklogs() {
        return backlogs;
    }
}
