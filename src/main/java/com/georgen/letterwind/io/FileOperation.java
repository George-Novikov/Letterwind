package com.georgen.letterwind.io;

import java.io.File;
import java.io.IOException;

public class FileOperation implements AutoCloseable {
    private FileFactory fileFactoryInstance;
    private File file;

    public FileOperation(String path, boolean isFileCreated) throws IOException {
        this.fileFactoryInstance = FileFactory.getInstance();
        this.file = fileFactoryInstance.getFile(path, isFileCreated);
    }

    public File getFile(){
        return this.file;
    }

    public boolean delete() {
        return fileFactoryInstance.delete(this.file);
    }

    public boolean isExistingFile() throws IOException {
        return this.file.exists();
    }

    @Override
    public void close() throws Exception {
        if (this.file != null) fileFactoryInstance.releaseFromCache(this.file);
    }
}
