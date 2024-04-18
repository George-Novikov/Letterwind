package com.georgen.letterwind.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CloseableScanner implements AutoCloseable {
    private Scanner scanner;

    protected CloseableScanner(File file) throws FileNotFoundException {
        this.scanner = new Scanner(file);
    }

    public String read(){
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    @Override
    public void close() throws Exception {
        if (this.scanner != null) this.scanner.close();
    }
}
