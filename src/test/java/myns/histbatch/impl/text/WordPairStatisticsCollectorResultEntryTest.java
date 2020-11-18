package myns.histbatch.impl.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import myns.histbatch.text.WordPairStatisticsCollector.ResultEntry;

class WordPairStatisticsCollectorResultEntryTest {

    @Test
    void testEquals() {
        ResultEntry entry1 = new ResultEntry("alma", "körte", 10);
        ResultEntry entry2 = new ResultEntry("alma", "körte", 10);
        
        assertThat(entry1).isEqualTo(entry2);
    }

    @Test
    void testPartiallyEquals() {
        ResultEntry entry1 = new ResultEntry("alma", "körte", 10);
        ResultEntry entry2 = new ResultEntry("alma", "narancs", 7);
        
        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testNotEquals() {
        ResultEntry entry1 = new ResultEntry("alma", "körte", 10);
        ResultEntry entry2 = new ResultEntry("apple", "pear", 99);
        
        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testHashCode() {
        ResultEntry entry1 = new ResultEntry("apple", "pear", 99);
        ResultEntry entry2 = new ResultEntry("apple", "pear", 99);
        
        assertThat(entry1.hashCode()).isEqualTo(entry2.hashCode());
    }

    @Test
    void testToString() {
        ResultEntry entry = new ResultEntry("alma", "körte", 10);
        String expected = "alma körte: 10";
        
        assertThat(entry).hasToString(expected);
    }

}
