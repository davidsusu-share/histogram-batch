package myns.histbatch;

import myns.histbatch.api.watcher.MockIncomingItemWatcher;

public class Main {

    public static void main(String[] args) {
        MockIncomingItemWatcher watcher = new MockIncomingItemWatcher();
        watcher.addListener(item -> System.out.println(item.name()));
        new Thread(watcher::watch).start();

        watcher.send("hello", "text", "Hello");
        watcher.send("world", "text", "World");
        watcher.send("lorem", "text", "Lorem");
        watcher.send("ipsum", "text", "Ipsum");
        
        watcher.stop();
        
        System.out.println("FINISHED");
    }
    
}
