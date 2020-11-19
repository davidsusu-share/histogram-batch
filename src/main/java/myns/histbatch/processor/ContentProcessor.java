package myns.histbatch.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Main interface for content processors
 */
@FunctionalInterface
public interface ContentProcessor {

    /**
     * Process the given content, and forward the result.
     * 
     * Neither <code>in</code> nor <code>out</code> will be closed,
     * even in case of exception.
     * 
     * @param in Input data stream
     * @param out Output data stream
     * @throws IOException
     */
    public void process(InputStream in, OutputStream out) throws IOException;
    
}
