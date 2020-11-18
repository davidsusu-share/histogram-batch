package myns.histbatch.watcher;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIncomingItemWatcher implements IncomingItemWatcher {

    private final List<IncomingItemListener> listeners = new ArrayList<>(1);
    
    
    @Override
    public synchronized void addListener(IncomingItemListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeListener(IncomingItemListener listener) {
        listeners.remove(listener);
    }

    protected synchronized void runListeners(IncomingItem incomingItem, SuccessCallback successCallback) {
        for (IncomingItemListener listener : listeners) {
            listener.receive(incomingItem, successCallback);
        }
    }
    
}
