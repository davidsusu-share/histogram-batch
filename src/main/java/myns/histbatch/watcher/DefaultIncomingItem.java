package myns.histbatch.watcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default {@IncomingItem} implementation
 */
public class DefaultIncomingItem implements IncomingItem {

    private final String name;

    private final IncomingItemType type;

    private final InputStreamFactory inputStreamFactory;
    

    public DefaultIncomingItem(String name, IncomingItemType type, String content) {
        this(name, type, content.getBytes(StandardCharsets.UTF_8));
    }

    public DefaultIncomingItem(String name, IncomingItemType type, byte[] content) {
        this(name, type, () -> new ByteArrayInputStream(content));
    }
    
    public DefaultIncomingItem(String name, IncomingItemType type, InputStreamFactory inputStreamFactory) {
        this.name = name;
        this.type = type;
        this.inputStreamFactory = inputStreamFactory;
    }
    

    @Override
    public String name() {
        return name;
    }

    @Override
    public IncomingItemType type() {
        return type;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return inputStreamFactory.create();
    }
    
    
    public interface InputStreamFactory {
        
        public InputStream create() throws IOException;
        
    }

}
