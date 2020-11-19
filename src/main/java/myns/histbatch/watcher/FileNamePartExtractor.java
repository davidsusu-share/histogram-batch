package myns.histbatch.watcher;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Utility for extracting main name of a file
 */
public class FileNamePartExtractor {
    
    private static final Pattern CLEAR_PATTERN = Pattern.compile("(?:^\\.)|(?:\\.[^\\.]+$)");
    

    /**
     * Extracting main name of the given file
     * 
     * Main name is calculated form basename,
     * removing leading dot and extension from it (if any).
     * 
     * @param file File to examine
     * @return The main name
     */
    public String extract(File file) {
        return CLEAR_PATTERN.matcher(file.getName()).replaceAll("");
    }
    
}
