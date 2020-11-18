package myns.histbatch.watcher;

@FunctionalInterface
public interface SuccessCallback {

    public void notify(boolean success);
    
}
