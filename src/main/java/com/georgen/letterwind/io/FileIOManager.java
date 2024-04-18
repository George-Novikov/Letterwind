package com.georgen.letterwind.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileIOManager {
    public static void write(File file, String content) throws Exception {
        synchronized (file){
            try (CloseableWriter writer = new CloseableWriter(file, false)){
                writer.append(content);
            }
        }
    }

    public static void append(File file, String content) throws Exception {
        synchronized (file){
            try (CloseableWriter writer = new CloseableWriter(file, true)){
                writer.append(content);
            }
        }
    }

    public static String read(File file) throws Exception {
        synchronized (file){
            try (CloseableReader reader = new CloseableReader(file)){
                return reader.read();
            }
        }
    }

    public static void writeBytes(File file, byte[] content) throws IOException {
        synchronized (file){
            Files.write(file.toPath(), content);
        }
    }

    public static byte[] readBytes(File file) throws IOException {
        synchronized (file){
            return Files.readAllBytes(file.toPath());
        }
    }
}
