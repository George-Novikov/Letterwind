package com.georgen.letterwind.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SystemHelper {
    public static boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }

    public static void setFilePermissions(File file) throws IOException {
        Set<PosixFilePermission> permissions = Arrays
                .stream(PosixFilePermission.values())
                .filter(permission -> !permission.name().startsWith("OTHERS"))
                .collect(Collectors.toSet());

        Files.setPosixFilePermissions(file.toPath(), permissions);
    }
}
