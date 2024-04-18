package com.georgen.letterwind.io;


import com.georgen.letterwind.model.constants.SystemProperty;
import com.georgen.letterwind.tools.SystemHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class has a cache that ensures that all requested files with the same path
 * will reference the same File object, thus synchronizing all input/output operations properly
 * */
public class FileFactory {
    private ConcurrentMap<String, File> fileCache;

    private FileFactory(){ this.fileCache = new ConcurrentHashMap<>(); }

    public File getFile(String path, boolean isCreated) throws IOException {
        File operatedFile = fileCache.get(path);
        if (operatedFile != null) return operatedFile;

        File file = new File(path);
        if (isCreated && !file.exists()) file = createFile(file);
        if (file.exists()) fileCache.put(path, file);

        return file;
    }

    public boolean delete(File file){
        String path = file.getPath();
        if (path.endsWith(SystemProperty.ID_COUNTER_NAME.getValue())) return false;

        AtomicBoolean isDeletedAtomically = new AtomicBoolean();

        synchronized (file){
            isDeletedAtomically.set(file.delete());
        }

        boolean isDeleted = isDeletedAtomically.get();
        if (isDeleted) fileCache.remove(path);

        return isDeleted;
    }

    public File releaseFromCache(File file){
        File releasedFile = fileCache.remove(file.getPath());
        return releasedFile;
    }

    private File createFile(File file) throws IOException {
        synchronized (file){
            file.getParentFile().mkdirs();
            file.createNewFile();
            setFilePermissions(file);
            return file;
        }
    }

    private void setFilePermissions(File file) throws IOException {
        if (!SystemHelper.isUnixSystem()) return;
        SystemHelper.setFilePermissions(file);
    }

    public static FileFactory getInstance() {
        /**
         * There is no direct threat to calling this class as a factory for regular files.
         *
         * But if all files created this way are not freed from the cache, it will lead to a memory leak.
         *
         * I omitted the caller class check to improve performance because Thread.currentThread().getStackTrace(); is a very expensive operation.
         *
         * So this task will have to be addressed in future releases.
         *
         * StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
         * boolean isPermittedCaller = PERMITTED_CALLERS.stream().anyMatch(caller -> caller.getName().equals(stackTrace[2].getClassName()));
         * if (!isPermittedCaller) throw new HawthorneException(Message.PERMISSION_DENIED);
        */
        return FileFactoryHolder.INSTANCE;
    }

    private static class FileFactoryHolder{
        private static final FileFactory INSTANCE = new FileFactory();
    }
}
