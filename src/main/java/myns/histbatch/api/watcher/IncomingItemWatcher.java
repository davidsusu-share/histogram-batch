package myns.histbatch.api.watcher;

import java.io.IOException;

public interface IncomingItemWatcher {

    public void watch() throws IOException;
    
    public void stop();
    
    public void addListener(IncomingItemListener listener);
    
    public void removeListener(IncomingItemListener listener);
    
}
