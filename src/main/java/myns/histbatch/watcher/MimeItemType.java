package myns.histbatch.watcher;

import java.util.Objects;

public class MimeItemType {
    
    private final String mimeType;
    

    public MimeItemType(String mimeType) {
        this.mimeType = Objects.requireNonNull(mimeType);
    }
    
    
    public String mimeType() {
        return mimeType;
    }
    
}
