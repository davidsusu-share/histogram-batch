package myns.histbatch.watcher;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Mock implementation of {@link IncomingItemWatcher}
 */
public class MockIncomingItemWatcher extends AbstractIncomingItemWatcher {

    private AtomicBoolean running = new AtomicBoolean(false);
    
    
    /**
     * Explicitly sends an item to the watcher
     * 
     * @param incomingItem The item
     */
    public synchronized void send(IncomingItem incomingItem) {
        if (running.get()) {
            runListeners(incomingItem, success -> {});
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

}
