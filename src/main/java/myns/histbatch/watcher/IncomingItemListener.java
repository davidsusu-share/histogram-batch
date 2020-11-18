package myns.histbatch.watcher;

@FunctionalInterface
public interface IncomingItemListener {

    public void receive(IncomingItem incomingItem, SuccessCallback successCallback);
    
}
