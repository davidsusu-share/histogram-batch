package myns.histbatch.watcher;

import java.io.IOException;
import java.io.OutputStream;

public interface IncomingItemOutputStreamFactory {

    public OutputStream openOutputStreamFor(
            IncomingItem incomingItem) throws IOException;
    
}
