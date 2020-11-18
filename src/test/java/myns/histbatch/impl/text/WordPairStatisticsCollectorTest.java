package myns.histbatch.impl.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import myns.histbatch.text.WordPairStatisticsCollector;
import myns.histbatch.text.WordPairStatisticsCollector.ResultEntry;

class WordPairStatisticsCollectorTest {

    @Test
    void testLongerText() throws IOException {
        WordPairStatisticsCollector collector = new WordPairStatisticsCollector(
                new Locale("hu_HU"));
        
        String text =
                "Lorem ipsum alma. " +
                "Körte lorem ipsum alma árpa körte.\n\n" +
                "Narancs dolores apple lorem. " +
                "Ipsum. Alma. Árpa körte x-y g2." +
                "Lorem ipsum, X-Y G2.";
        
        Reader reader = new StringReader(text);
        
        List<ResultEntry> actual = collector.collect(reader, 5);
        
        List<ResultEntry> expected = List.of(
                new ResultEntry("lorem", "ipsum", 3),
                new ResultEntry("árpa", "körte", 2),
                new ResultEntry("ipsum", "alma", 2),
                new ResultEntry("x-y", "g2", 2),
                new ResultEntry("alma", "árpa", 1));
        
        assertThat(actual).isEqualTo(expected);
    }

}
