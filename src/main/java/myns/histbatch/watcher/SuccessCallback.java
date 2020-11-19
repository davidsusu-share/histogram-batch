package myns.histbatch.watcher;

/**
 * Simple callback for indicating process status
 */
@FunctionalInterface
public interface SuccessCallback {

    /**
     * Notifies the caller of the success status
     * 
     * Do not call this if the processing was skipped
     * (e.g. in case of unmatching filter).
     * 
     * @param success True if the process was successful
     */
    public void completed(boolean success);
    
}
