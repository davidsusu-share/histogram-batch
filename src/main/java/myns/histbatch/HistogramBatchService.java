package myns.histbatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import myns.histbatch.image.GrayScaleHistogramCollector;
import myns.histbatch.image.LuminancePreservingGrayScaleExtractor;
import myns.histbatch.processor.ImageGrayScaleHistogramContentProcessor;
import myns.histbatch.processor.WordPairStatisticsContentProcessor;
import myns.histbatch.text.WordPairStatisticsCollector;
import myns.histbatch.watcher.ContentProcessorIncomingItemListener;
import myns.histbatch.watcher.IncomingItem;
import myns.histbatch.watcher.IncomingItemWatcher;
import myns.histbatch.watcher.MimeItemTypeMatcher;
import myns.histbatch.watcher.WatchServiceIncomingItemWatcher;

public class HistogramBatchService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(
            HistogramBatchService.class);
    
    
    private static final MimeItemTypeMatcher IMAGE_MATCHER =
            new MimeItemTypeMatcher("image/*");
    
    private static final MimeItemTypeMatcher TEXT_MATCHER =
            new MimeItemTypeMatcher("text/plain");
    
    private static final String DONE_SUFFIX = "-done";
    
    private static final String ERROR_SUFFIX = "-error";
    
    
    private final Path directory;
    
    private final int keepAliveSeconds;
    
    private final int threadCount;
    
    private final Locale locale;
    
    private final int wordPairsMaxIgnore;
    

    public HistogramBatchService(String directory) {
        this(Path.of(directory));
    }
    
    public HistogramBatchService(Path directory) {
        this(builder().directory(directory).builder());
    }

    private HistogramBatchService(Builder builder) {
        this.directory = builder.directory;
        this.keepAliveSeconds = builder.keepAliveSeconds;
        this.threadCount = builder.threadCount;
        this.locale = builder.locale;
        this.wordPairsMaxIgnore = builder.wordPairsMaxIgnore;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    public void run() {
        try {
            runInternal();
        } catch (IOException e) {
            logger.error("{} stopped unexpectedly", getClass().getSimpleName(), e);
        }
    }
    
    private void runInternal() throws IOException {
        logger.info("{} started at {}", getClass().getSimpleName(), directory);
        
        File directoryFile = directory.toFile();
        
        if (!directoryFile.exists()) {
            Files.createDirectories(directory);
        }
        
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        
        IncomingItemWatcher watcher = new WatchServiceIncomingItemWatcher(
                directory,
                keepAliveSeconds,
                this::onComplete);
        
        watcher.addListener(new ContentProcessorIncomingItemListener(
                item -> openOutputStreamFor(item, "image", "-hist.bmp"),
                new ImageGrayScaleHistogramContentProcessor(
                        new GrayScaleHistogramCollector(
                                new LuminancePreservingGrayScaleExtractor())),
                item -> IMAGE_MATCHER.test(item.type()),
                executorService));

        watcher.addListener(new ContentProcessorIncomingItemListener(
                item -> openOutputStreamFor(item, "text", ".txt"),
                new WordPairStatisticsContentProcessor(
                        new WordPairStatisticsCollector(
                                locale, wordPairsMaxIgnore)),
                item -> TEXT_MATCHER.test(item.type()),
                executorService));
        
        watcher.watch();
        
        executorService.shutdown();
    }
    
    private OutputStream openOutputStreamFor(
            IncomingItem item, String prefix, String suffix)
            throws IOException {
        
        File targetDirectory = new File(directory.toFile(), prefix + DONE_SUFFIX);

        if (!targetDirectory.exists()) {
            Files.createDirectories(targetDirectory.toPath());
        }
        
        String targetFilename = item.name() + suffix;
        File targetFile = new File(targetDirectory, targetFilename);
        
        logger.debug("Open file for writing output: {}", targetFile);
        
        return new FileOutputStream(targetFile);
    }
    
    private void onComplete(IncomingItem item, File file, boolean success) throws IOException {
        if (success) {
            onSuccess(file);
        } else {
            onFail(item, file);
        }
    }

    private void onSuccess(File file) throws IOException {
        Files.delete(file.toPath());
    }

    private void onFail(IncomingItem item, File file) throws IOException {
        File targetDirectory = new File(
                directory.toFile(), item.type().shortName() + ERROR_SUFFIX);

        if (!targetDirectory.exists()) {
            Files.createDirectories(targetDirectory.toPath());
        }
        
        File targetFile = new File(targetDirectory, file.getName());

        Files.move(file.toPath(), targetFile.toPath());
    }
    
    
    public static class Builder {

        private Path directory = null;

        private int keepAliveSeconds = 30;
        
        private int threadCount = 1;
        
        private Locale locale = Locale.getDefault();
        
        private int wordPairsMaxIgnore = 5;
        
        
        private Builder() {
            // use builder() instead
        }
        
        
        public Optionals directory(String directory) {
            return directory(Path.of(directory));
        }

        public Optionals directory(Path directory) {
            this.directory = directory;
            return new Optionals();
        }
        
        
        public class Optionals {
            
            private Optionals() {
                // for internal use only
            }
            

            public Optionals keepAliveSeconds(int keepAliveSeconds) {
                Builder.this.keepAliveSeconds = keepAliveSeconds;
                return this;
            }

            public Optionals threadCount(int threadCount) {
                Builder.this.threadCount = threadCount;
                return this;
            }
            
            public Optionals locale(Locale locale) {
                Builder.this.locale = locale;
                return this;
            }
            
            public Optionals wordPairsMaxIgnore(int wordPairsMaxIgnore) {
                Builder.this.wordPairsMaxIgnore = wordPairsMaxIgnore;
                return this;
            }
            
            
            public HistogramBatchService build() {
                return new HistogramBatchService(builder());
            }

            private Builder builder() {
                return Builder.this;
            }
            
        }
        
    }

}
