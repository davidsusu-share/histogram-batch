package myns.histbatch;

import myns.histbatch.api.watcher.IncomingItem;
import myns.histbatch.api.watcher.MockIncomingItemWatcher;

public class Main {

    public static void main(String[] args) {
        MockIncomingItemWatcher watcher = new MockIncomingItemWatcher();
        watcher.addListener(item -> System.out.println(item.name()));
        new Thread(watcher::watch).start();

        watcher.send(IncomingItem.of("hello", "text", "Hello"));
        watcher.send(IncomingItem.of("world", "text", "World"));
        watcher.send(IncomingItem.of("lorem", "text", "Lorem"));
        watcher.send(IncomingItem.of("ipsum", "text", "Ipsum"));
        
        watcher.stop();
        
        System.out.println("FINISHED");
    }
    
}
