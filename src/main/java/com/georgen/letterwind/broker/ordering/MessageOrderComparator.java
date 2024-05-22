package com.georgen.letterwind.broker.ordering;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;

public class MessageOrderComparator implements Comparator<Path> {
    @Override
    public int compare(Path path1, Path path2) {
        long order1 = extractOrderNumber(path1);
        long order2 = extractOrderNumber(path2);

        return Long.compare(order1, order2);
    }

    private long extractOrderNumber(Path path){
        String stringPath = path.toString();
        int separatorIndex = stringPath.lastIndexOf(File.separator);
        String fileName = stringPath.substring(++separatorIndex);
        String[] parts = fileName.split("-");
        if (parts.length < 2) return 0;
        return Long.valueOf(parts[0]);
    }
}
