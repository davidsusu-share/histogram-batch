package myns.histbatch;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "HistogramBatch", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true)
public class Main implements Runnable {

    @Option(names = "-i", description = "Directory to listen", arity = "1", required = true)
    public String directory;

    @Option(names = "-d", description = "Keep alive seconds", arity = "1", required = false)
    public int keepAliveSeconds = 60;

    @Option(names = "-t", description = "Thread count", arity = "1", required = false)
    public int threadCount = 1;

    @Option(names = "-n", description = "Maximum number of word pairs to ignore",
            arity = "1", required = false)
    public int wordPairsMaxIgnore = 3;
    
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args); 
        System.exit(exitCode);
    }
    

    @Override
    public void run() {
        HistogramBatchService.builder()
                .directory(directory)
                .keepAliveSeconds(keepAliveSeconds)
                .threadCount(threadCount)
                .wordPairsMaxIgnore(wordPairsMaxIgnore)
                .build()
                .run();
    }
    
}
