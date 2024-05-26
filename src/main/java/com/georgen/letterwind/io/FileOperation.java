package com.georgen.letterwind.io;

import com.georgen.letterwind.broker.ordering.IsolatedPaths;
import com.georgen.letterwind.broker.ordering.MessageOrderComparator;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class FileOperation implements AutoCloseable {
    private static final MessageOrderComparator MESSAGE_ORDER_COMPARATOR = new MessageOrderComparator();
    private FileFactory fileFactoryInstance;
    private AtomicInteger readAttemptsCounter;
    private int readAttemptsLimit;
    private File file;

    public FileOperation(String path) throws IOException {
        this(path, false);
    }

    public FileOperation(String path, boolean isFileCreated) throws IOException {
        this.fileFactoryInstance = FileFactory.getInstance();
        this.file = fileFactoryInstance.getFile(path, isFileCreated);
        this.readAttemptsCounter = new AtomicInteger(0);
        this.readAttemptsLimit = Configuration.getInstance().getFaultyMessageReadAttempts();
    }

    public File getFile(){
        return this.file;
    }

    public File getFirstFromDirectory() throws Exception {
        if (!this.file.isDirectory()) throw new LetterwindException("The requested path is not a directory.");

        synchronized (this.file){
            Path firstPath = null;

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){
                firstPath = StreamSupport
                        .stream(directoryStream.spliterator(), false)
                        .sorted(MESSAGE_ORDER_COMPARATOR)
                        .filter(path -> isAvailablePath(path))
                        .findFirst()
                        .orElse(null);

                if (firstPath == null) return null;
                IsolatedPaths.getInstance().isolate(firstPath);

                return fileFactoryInstance.getFile(firstPath, false);
            } catch (Exception e){
                /** This prevents an infinite recursive call */
                if (this.readAttemptsCounter.incrementAndGet() >= readAttemptsLimit) return null;
                if (firstPath != null) IsolatedPaths.getInstance().isolate(firstPath);
                return getFirstFromDirectory();
            }
        }
    }

    private boolean isAvailablePath(Path path){
        return !IsolatedPaths.getInstance().isIsolated(path)
                && !fileFactoryInstance.isCached(path)
                && path.toFile().exists();
    }

    public void cache(File file){ fileFactoryInstance.cache(file); }

    public void cache(Path path){ fileFactoryInstance.cache(path); }

    public File getFileByPath(Path path) throws IOException {
        return fileFactoryInstance.getFile(path.toString(), false);
    }

    public String read(File file) throws Exception {
        return FileIOManager.read(file);
    }

    public boolean delete() {
        return fileFactoryInstance.delete(this.file);
    }

    public boolean delete(File file) {
        IsolatedPaths.getInstance().release(file.toPath());
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
