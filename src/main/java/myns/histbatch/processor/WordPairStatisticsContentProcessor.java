package myns.histbatch.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import myns.histbatch.text.WordPairStatisticsCollector;

public class WordPairStatisticsContentProcessor implements ContentProcessor {

    private final WordPairStatisticsCollector collector;
    
    
    public WordPairStatisticsContentProcessor(
            WordPairStatisticsCollector collector) {
        this.collector = collector;
    }


    @Override
    public void process(InputStream in, OutputStream out) throws IOException {
        collector.collect(new InputStreamReader(in));
    }
    
}
