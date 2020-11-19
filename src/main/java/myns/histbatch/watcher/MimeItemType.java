package myns.histbatch.watcher;

import java.util.Objects;
import java.util.regex.Pattern;

public class MimeItemType implements IncomingItemType {
    
    private static final Pattern SHORTENER_PATTERN = Pattern.compile("/.*$");

    
    private final String mimeType;
    
    private final String shortName;
    

    public MimeItemType(String mimeType) {
        this.mimeType = Objects.requireNonNull(mimeType);
        this.shortName = SHORTENER_PATTERN.matcher(mimeType).replaceAll("");
    }
    
    
    public String mimeType() {
        return mimeType;
    }

    @Override
    public String shortName() {
        return shortName;
    }
    
    @Override
    public String toString() {
        return mimeType;
    }
    
    @Override
    public int hashCode() {
        return mimeType.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MimeItemType)) {
            return false;
        }
        
        MimeItemType otherMimeItemType = (MimeItemType) other;
        
        return mimeType.equals(otherMimeItemType.mimeType);
    }

}
