package myns.histbatch.api.watcher;

public interface IncomingItemWatcher {

    public void watch();
    
    public void stop();
    
    public void addListener(IncomingItemListener listener);
    
    public void removeListener(IncomingItemListener listener);
    
}
