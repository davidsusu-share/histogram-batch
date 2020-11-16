package myns.histbatch.impl.watcher;

import java.io.File;
import java.util.regex.Pattern;

public class FileNamePartExtractor {
    
    private static final Pattern CLEAR_PATTERN = Pattern.compile("(?:^\\.)|(?:\\.[^\\.]+$)");
    

    public String extract(File file) {
        return CLEAR_PATTERN.matcher(file.getName()).replaceAll("");
    }
    
}
