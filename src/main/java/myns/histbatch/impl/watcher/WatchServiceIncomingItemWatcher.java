package myns.histbatch.impl.watcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import myns.histbatch.api.watcher.AbstractIncomingItemWatcher;
import myns.histbatch.api.watcher.IncomingItem;

public class WatchServiceIncomingItemWatcher extends AbstractIncomingItemWatcher {

    private static final int DEFAULT_KEEPALIVE_SECONDS = 60;
    
    private static final WatchEvent.Kind<Path> EVENT_KIND = StandardWatchEventKinds.ENTRY_CREATE;
    
    
    private static final Logger logger = LoggerFactory.getLogger(
            WatchServiceIncomingItemWatcher.class);
    
    
    private final Path path;

    private final int keepAliveSeconds;
    
    private final CompletionCallback completionCallback;
    

    public WatchServiceIncomingItemWatcher(Path path) {
        this(path, DEFAULT_KEEPALIVE_SECONDS);
    }
    
    public WatchServiceIncomingItemWatcher(Path path, int keepAliveSeconds) {
        this(path, keepAliveSeconds, (file, success) -> {});
    }
    
    public WatchServiceIncomingItemWatcher(
            Path path, int keepAliveSeconds, CompletionCallback completionCallback) {
        
        this.path = Objects.requireNonNull(path);
        this.keepAliveSeconds = Objects.requireNonNull(keepAliveSeconds);
        this.completionCallback = Objects.requireNonNull(completionCallback);
    }
    
    
    @Override
    public synchronized void watch() throws IOException {
        watchInternal(FileSystems.getDefault().newWatchService());
    }

    private void watchInternal(WatchService watchService) throws IOException {
        try {
            runWatchService(watchService);
        } catch (IOException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new IOException("Watcher stopped unexpectedly", e);
        }
    }
    
    private void runWatchService(WatchService watchService)
            throws IOException, InterruptedException {
        
        path.register(watchService, EVENT_KIND);
        while (true) {
            WatchKey key = watchService.poll(keepAliveSeconds, TimeUnit.SECONDS);
            if (!acceptKey(key)) {
                break;
            }
        }
    }

    private boolean acceptKey(WatchKey key) {
        if (key == null) {
            return false;
        }
        
        handleKey(key);

        return key.reset();
    }

    private void handleKey(WatchKey key) {
        for (WatchEvent<?> event: key.pollEvents()) {
            acceptEvent(event);
        }
    }
    
    private void acceptEvent(WatchEvent<?> event) {
        if (event.kind() != EVENT_KIND) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
        File file = pathEvent.context().toFile();
        if (!file.isFile()) {
            return;
        }
        
        handleFile(file);
    }
    
    private void handleFile(File file) {
        Object type = detectItemType(file);
        if (type == null) {
            handleCompleted(file, false);
            return;
        }
        
        String name = new FileNamePartExtractor().extract(file);
        IncomingItem incomingItem = IncomingItem.of(name, type, () -> new FileInputStream(file));
        runListeners(incomingItem, success -> handleCompleted(file, success));
    }
   
    private Object detectItemType(File file) {
        String mimeType = null;
        try {
            mimeType = detectFileMimeType(file);
        } catch (IOException e) {
            logger.error("Can not detect file type: {}", file, e);
        }
        
        return mimeType != null ? new MimeItemType(mimeType) : null;
    }

    private String detectFileMimeType(File file) throws IOException {
        return Files.probeContentType(file.toPath());
    }
    
    private void handleCompleted(File file, boolean success) {
        if (!success) {
            logger.warn("Unable to process file: {}", file);
        }
        
        try {
            completionCallback.completed(file, success);
        } catch (Exception e) {
            logger.error("Failed to run callback for: {}", file, e);
        }
        
        if (success) {
            logger.info("Successfully processed file: {}", file);
        }
    }
    
    
    @FunctionalInterface
    public interface CompletionCallback {
        
        public void completed(File file, boolean success) throws Exception;
        
    }

}
