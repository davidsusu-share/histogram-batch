package myns.histbatch.watcher;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Factory for open output stream associated to an {@link IncomingItem}
 */
@FunctionalInterface
public interface IncomingItemOutputStreamFactory {

    /**
     * Opens a new {@link OutputStream} for the given {@link IncomingItem}
     * 
     * @param incomingItem Incoming item
     * @return Output stream for the processed content
     * @throws IOException
     */
    public OutputStream openOutputStreamFor(
            IncomingItem incomingItem) throws IOException;
    
}
