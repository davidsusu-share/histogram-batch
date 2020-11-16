package myns.histbatch.api.watcher;

import java.io.IOException;
import java.io.InputStream;

import myns.histbatch.api.watcher.DefaultIncomingItem.InputStreamFactory;

public interface IncomingItem {

    public String name();
    
    public Object type();
    
    public InputStream openInputStream() throws IOException;
    

    public static IncomingItem of(String name, Object type, String content) {
        return new DefaultIncomingItem(name, type, content);
    }

    public static IncomingItem of(String name, Object type, byte[] content) {
        return new DefaultIncomingItem(name, type, content);
    }

    public static IncomingItem of(String name, Object type, InputStreamFactory inputStreamFactory) {
        return new DefaultIncomingItem(name, type, inputStreamFactory);
    }

}
