package com.georgen.letterwind.broker.counters;

import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

public class AtomicCounter {
    private File messageTypeDirectory;
    private AtomicLong count;

    public AtomicCounter(File messageTypeDirectory){
        this.messageTypeDirectory = messageTypeDirectory;
        this.count = new AtomicLong();
    }

    public File getMessageTypeDirectory() {
        return messageTypeDirectory;
    }

    public void setMessageTypeDirectory(File messageTypeDirectory) {
        this.messageTypeDirectory = messageTypeDirectory;
    }

    public long count() throws LetterwindException, IOException {
        countDirectoryContents();
        return this.count.get();
    }


    private void countDirectoryContents() throws LetterwindException, IOException {
        if (!this.messageTypeDirectory.isDirectory()) throw new LetterwindException("The requested path is not a directory.");

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.messageTypeDirectory.toPath())){
            long count = StreamSupport.stream(directoryStream.spliterator(), false).count();
            this.count.set(count);
        }
    }
}
