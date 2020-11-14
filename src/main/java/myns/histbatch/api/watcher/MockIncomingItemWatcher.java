package myns.histbatch.api.watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MockIncomingItemWatcher implements IncomingItemWatcher {

    private final List<IncomingItemListener> listeners = new ArrayList<>(1);
    
    private AtomicBoolean running = new AtomicBoolean(false);
    
    
    public synchronized void send(IncomingItem incomingItem) {
        if (running.get()) {
            runListeners(incomingItem);
        }
    }
    
    private void runListeners(IncomingItem incomingItem) {
        for (IncomingItemListener listener : listeners) {
            listener.receive(incomingItem);
        }
    }
    
    @Override
    public void watch() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Watcher is already running");
        }
        
        while (running.get()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void stop() {
        running.set(false);
    }

    @Override
    public synchronized void addListener(IncomingItemListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeListener(IncomingItemListener listener) {
        listeners.remove(listener);
    }
    
}
