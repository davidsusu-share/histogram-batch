package myns.histbatch.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@FunctionalInterface
public interface ContentProcessor {

    public void process(InputStream in, OutputStream out) throws IOException;
    
}
