package myns.histbatch.watcher;

import java.io.IOException;

/**
 * Interface for incoming item watchers
 */
public interface IncomingItemWatcher {

    /**
     * Runs this watcher, and wait until finish
     * 
     * @throws IOException
     */
    public void watch() throws IOException;

    /**
     * Stops this watcher.
     * 
     * This is an optional operation.
     * Can be called from an other thread than
     * the one <code>watch()</code> was invoked on.
     */
    public default void stop() {
        throw new UnsupportedOperationException(
                "This watcher does not support explicit stop");
    }
    
    /**
     * Registers a listener to this watcher
     * 
     * @param listener The listener
     */
    public void addListener(IncomingItemListener listener);

    /**
     * Removes a listener from this watcher
     * 
     * @param listener The listener
     */
    public void removeListener(IncomingItemListener listener);
    
}
