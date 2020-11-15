package myns.histbatch.api.watcher;

@FunctionalInterface
public interface IncomingItemListener {

    public void receive(IncomingItem incomingItem);
    
}
