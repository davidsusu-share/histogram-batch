package myns.histbatch.watcher;

/**
 * Incoming item listener interface used by {@link IncomingItemWatcher}
 */
@FunctionalInterface
public interface IncomingItemListener {

    /**
     * Callback for notifying the watcher about the success status of a processing.
     * 
     * Do not call this if the processing was skipped
     * (e.g. in case of unmatching filter).
     * 
     * @param incomingItem The incoming item
     * @param successCallback True if the processing was successful
     */
    public void receive(IncomingItem incomingItem, SuccessCallback successCallback);
    
}
