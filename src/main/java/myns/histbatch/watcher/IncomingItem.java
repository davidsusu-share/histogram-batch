package myns.histbatch.watcher;

import java.io.IOException;
import java.io.InputStream;

import myns.histbatch.watcher.DefaultIncomingItem.InputStreamFactory;

/**
 * Interface for providing and abstraction for incoming content items
 */
public interface IncomingItem {

    public String name();
    
    public IncomingItemType type();
    
    public InputStream openInputStream() throws IOException;
    

    public static IncomingItem of(String name, IncomingItemType type, String content) {
        return new DefaultIncomingItem(name, type, content);
    }

    public static IncomingItem of(String name, IncomingItemType type, byte[] content) {
        return new DefaultIncomingItem(name, type, content);
    }

    public static IncomingItem of(String name, IncomingItemType type, InputStreamFactory inputStreamFactory) {
        return new DefaultIncomingItem(name, type, inputStreamFactory);
    }

}
