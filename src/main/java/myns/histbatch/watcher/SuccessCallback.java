package myns.histbatch.watcher;

@FunctionalInterface
public interface SuccessCallback {

    public void completed(boolean success);
    
}
