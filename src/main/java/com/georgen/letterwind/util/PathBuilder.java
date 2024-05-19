package com.georgen.letterwind.util;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PathBuilder {

    public static String concatenate(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String concatenate(String... paths){
        if (paths == null || paths.length < 1) return File.separator;
        return Arrays.stream(paths).collect(Collectors.joining(File.separator));
    }

    public static String formatSeparators(String string){
        try {
            /** Currently only custom paths relative to the root are supported */
            if (string.startsWith("/") || string.startsWith("\\")) string = string.substring(1);
            return string.replace("/", File.separator).replace("\\", File.separator);
        } catch (Exception e){
            return string;
        }
    }
}
