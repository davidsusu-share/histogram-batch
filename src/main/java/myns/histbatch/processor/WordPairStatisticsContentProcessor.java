package myns.histbatch.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import myns.histbatch.text.WordPairStatisticsCollector;
import myns.histbatch.text.WordPairStatisticsCollector.ResultEntry;

public class WordPairStatisticsContentProcessor implements ContentProcessor {

    private final WordPairStatisticsCollector collector;
    
    
    public WordPairStatisticsContentProcessor(
            WordPairStatisticsCollector collector) {
        this.collector = collector;
    }


    @Override
    public void process(InputStream in, OutputStream out) throws IOException {
        List<ResultEntry> entries = collector.collect(
                new InputStreamReader(in, StandardCharsets.UTF_8));
        
        for (ResultEntry entry : entries) {
            out.write(entry.toString().getBytes(StandardCharsets.UTF_8));
            out.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }
    }
    
}
