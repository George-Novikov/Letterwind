package com.georgen.letterwind.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;

public class CloseableReader implements AutoCloseable {
    private BufferedReader reader;

    protected CloseableReader(File file) throws FileNotFoundException {
        this.reader = new BufferedReader(
                new FileReader(file)
        );
    }

    public String read(){
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public void close() throws Exception {
        if (this.reader != null) this.reader.close();
    }
}
