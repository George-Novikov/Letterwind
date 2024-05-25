package com.georgen.letterwind.io;

import com.georgen.letterwind.broker.ordering.MessageOrderComparator;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

public class FileOperation implements AutoCloseable {
    private static final MessageOrderComparator MESSAGE_ORDER_COMPARATOR = new MessageOrderComparator();
    private FileFactory fileFactoryInstance;
    private File file;

    public FileOperation(String path) throws IOException {
        this(path, false);
    }

    public FileOperation(String path, boolean isFileCreated) throws IOException {
        this.fileFactoryInstance = FileFactory.getInstance();
        this.file = fileFactoryInstance.getFile(path, isFileCreated);
    }

    public File getFile(){
        return this.file;
    }

    public File getFirstFromDirectory() throws Exception {
        if (!this.file.isDirectory()) throw new LetterwindException("The requested path is not a directory.");

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){
            Path firstMessagePath = StreamSupport
                    .stream(directoryStream.spliterator(), false)
                    .sorted(MESSAGE_ORDER_COMPARATOR)
                    .findFirst()
                    .orElse(null);

            if (firstMessagePath == null) return null;

            return firstMessagePath.toFile();
        }
    }

    public void cache(File file){ fileFactoryInstance.cache(file); }

    public boolean delete() {
        return fileFactoryInstance.delete(this.file);
    }

    public boolean delete(File file) {
        return fileFactoryInstance.delete(file);
    }

    public void mkdirsOrBypass(){
        if (!file.exists()) file.mkdirs();
    }

    public long countContents() throws LetterwindException, IOException {
        if (!this.file.isDirectory()) throw new LetterwindException("The requested path is not a directory.");

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){
            return StreamSupport.stream(directoryStream.spliterator(), false).count();
        }
    }

    public boolean isExistingFile() {
        return this.file.exists();
    }

    @Override
    public void close() throws Exception {
        if (this.file != null) fileFactoryInstance.releaseFromCache(this.file);
    }
}
