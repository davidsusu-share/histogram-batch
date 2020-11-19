package myns.histbatch;

public class Main {

    public static void main(String[] args) {
        
        // TODO: args
        
        HistogramBatchService.builder()
                .directory("/home/david/develop/pf/2020.11-neti/input/INCOMING")
                .threadCount(2)
                .build()
                .run();
    }
    
}
