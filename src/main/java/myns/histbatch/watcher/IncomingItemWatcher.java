package myns.histbatch.watcher;

import java.io.IOException;

public interface IncomingItemWatcher {

    public void watch() throws IOException;
    
    public default void stop() {
        throw new UnsupportedOperationException(
                "This watcher does not support explicit stop");
    }
    
    public void addListener(IncomingItemListener listener);
    
    public void removeListener(IncomingItemListener listener);
    
}
