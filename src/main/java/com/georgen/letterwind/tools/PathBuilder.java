package com.georgen.letterwind.tools;

import java.io.File;

/**
 * SingletonEntity path: hawthorneRoot/entities/customPath/classSimpleName/classSimpleName.json
 * BinaryData (singleton) path: hawthorneRoot/entities/customPath/classSimpleName/classSimpleName.bin
 *
 * EntityCollection path: hawthorneRoot/entities/customPath/classSimpleName/partitionNumber/id.json
 * BinaryData (collection) path: hawthorneRoot/entities/customPath/classSimpleName/partitionNumber/id.bin
 */
public class PathBuilder {

    public static String concatenate(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
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
