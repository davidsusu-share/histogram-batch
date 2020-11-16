package myns.histbatch;

import java.io.File;
import java.io.IOException;

import myns.histbatch.api.watcher.IncomingItemWatcher;
import myns.histbatch.impl.watcher.WatchServiceIncomingItemWatcher;

public class Main {

    public static void main(String[] args) throws IOException {
        
        // FIXME
        String path = "/home/david/develop/pf/2020.11-neti/input";
        int keepAliveSeconds = 30;
        
        IncomingItemWatcher watcher = new WatchServiceIncomingItemWatcher(
                new File(path).toPath(), keepAliveSeconds);
        watcher.watch();
        
        // TODO
        
    }
    
}
