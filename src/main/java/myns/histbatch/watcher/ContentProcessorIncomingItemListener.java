package myns.histbatch.watcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import myns.histbatch.processor.ContentProcessor;

public class ContentProcessorIncomingItemListener implements IncomingItemListener {
    
    private static final Logger logger = LoggerFactory.getLogger(
            ContentProcessorIncomingItemListener.class);
    

    private final IncomingItemOutputStreamFactory outputStreamFactory;
    
    private final ContentProcessor contentProcessor;
    
    private final Predicate<IncomingItem> filter;
    
    private final Executor executor;
    
    
    public ContentProcessorIncomingItemListener(
            IncomingItemOutputStreamFactory outputStreamFactory,
            ContentProcessor contentProcessor) {
        
        this(outputStreamFactory, contentProcessor, anyItem -> true);
    }

    public ContentProcessorIncomingItemListener(
            IncomingItemOutputStreamFactory outputStreamFactory,
            ContentProcessor contentProcessor,
            Predicate<IncomingItem> filter) {

        this(outputStreamFactory, contentProcessor, filter, Executors.newSingleThreadExecutor());
    }
    
    public ContentProcessorIncomingItemListener(
            IncomingItemOutputStreamFactory outputStreamFactory,
            ContentProcessor contentProcessor,
            Predicate<IncomingItem> filter,
            Executor executor) {

        this.outputStreamFactory = outputStreamFactory;
        this.contentProcessor = contentProcessor;
        this.filter = filter;
        this.executor = executor;
    }
    
    
    @Override
    public void receive(IncomingItem incomingItem, SuccessCallback successCallback) {
        String itemName = incomingItem.name();
        
        if (!filter.test(incomingItem)) {
            return;
        }
        
        logger.info("Detected new item: {}", itemName);
        
        try {
            executor.execute(() -> handleItem(incomingItem, successCallback));
        } catch (Exception e) {
            logger.error("Failed to process item: {}", itemName, e);
            successCallback.completed(false);
        }
    }

    private void handleItem(IncomingItem incomingItem, SuccessCallback successCallback) {
        String itemName = incomingItem.name();
        
        try {
            handleItemThrows(incomingItem);
            successCallback.completed(true);
            logger.info("Item successfully processed: {}", itemName);
        } catch (IOException e) {
            logger.error("Failed to process item: {}", itemName, e);
            successCallback.completed(false);
        }
    }
    
    private void handleItemThrows(IncomingItem incomingItem) throws IOException {
        try (
                InputStream in = incomingItem.openInputStream();
                OutputStream out = outputStreamFactory.openOutputStreamFor(incomingItem);
                ) {
            contentProcessor.process(in, out);
        }
    }
    
}
