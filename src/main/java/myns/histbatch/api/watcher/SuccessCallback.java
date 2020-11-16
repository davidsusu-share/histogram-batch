package myns.histbatch.api.watcher;

@FunctionalInterface
public interface SuccessCallback {

    public void notify(boolean success);
    
}
