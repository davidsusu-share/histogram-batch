package myns.histbatch.api;

import java.io.IOException;
import java.io.OutputStream;

public interface ProcessingSpec {

    public Object type();
    
    public ContentProcessor contentProcessor();
    
    public OutputStream outputStream() throws IOException;
    
}
