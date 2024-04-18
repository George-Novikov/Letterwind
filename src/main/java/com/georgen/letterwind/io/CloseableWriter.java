package com.georgen.letterwind.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CloseableWriter implements AutoCloseable {
    private FileWriter fileWriter;

    protected CloseableWriter(File file, boolean isAppended) throws IOException {
        this.fileWriter = new FileWriter(file, isAppended);
    }

    public Writer append(String content) throws IOException {
        return fileWriter.append(content);
    }

    @Override
    public void close() throws Exception {
        if (this.fileWriter != null) fileWriter.close();
    }
}
