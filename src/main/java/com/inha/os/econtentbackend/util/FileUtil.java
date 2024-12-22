package com.inha.os.econtentbackend.util;

public class FileUtil {
    /**
     * Extracts the extension of a file from its name.
     *
     * @param fileName The name of the file.
     * @return The file extension, or an empty string if no extension is found.
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // No extension for null or empty file names
        }

        int lastDotIndex = fileName.lastIndexOf('.');

        // Check if there's a dot and it's not the first character
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1); // Return the extension
        }

        return ""; // No extension found
    }
}
