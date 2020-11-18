package myns.histbatch.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class WordPairStatisticsCollector {
    
    private enum CharType { WORD, SPACE, OTHER }
    
    
    private final Collator collator;
    
    private final ThreadLocal<State> stateHolder = ThreadLocal.withInitial(State::new);
    
    
    public WordPairStatisticsCollector() {
        this(Locale.getDefault());
    }

    public WordPairStatisticsCollector(Locale locale) {
        this.collator = Collator.getInstance(locale);
    }
    

    public List<ResultEntry> collect(Reader reader, int resultLimit) throws IOException {
        Reader bufferedReader = new BufferedReader(reader);
        
        int code;
        while ((code = bufferedReader.read()) != -1) {
            char ch = (char) code;
            acceptChar(ch);
        }
        doFinal();
        
        List<ResultEntry> result = extractResult(resultLimit);
        stateHolder.remove();
        return result;
    }
    
    private void acceptChar(char ch) {
        State state = stateHolder.get();
        CharType charType = detectCharType(ch);

        if (charType == CharType.WORD) {
            state.currentWordBuilder.append(ch);
        } else if (state.previousCharType == CharType.WORD) {
            terminateWord();
        }
        
        boolean isPairSeparator =
                charType == CharType.OTHER ||
                (charType == CharType.SPACE && state.previousCharType != CharType.WORD);
        if (isPairSeparator) {
            state.previousWord = null;
        }
        
        state.previousCharType = charType;
    }
    
    private CharType detectCharType(char ch) {
        if (ch == ' ' || ch == '\t') {
            return CharType.SPACE;
        } else if (
                Character.isAlphabetic(ch) ||
                Character.isDigit(ch) ||
                ch == '-') {
            return CharType.WORD;
        } else {
            return CharType.OTHER;
        }
    }
    
    private void doFinal() {
        State state = stateHolder.get();
        
        if (state.previousCharType == CharType.WORD) {
            terminateWord();
        }
    }

    private void terminateWord() {
        State state = stateHolder.get();
        
        String newWord = state.currentWordBuilder.toString();
        state.currentWordBuilder = new StringBuilder();
        
        if (state.previousWord != null) {
            registerWordPair(state.previousWord, newWord);
        }
        
        state.previousWord = newWord;
    }
    
    private void registerWordPair(String word1, String word2) {
        State state = stateHolder.get();

        String normalizedWord1 = word1.toLowerCase();
        String normalizedWord2 = word2.toLowerCase();
        
        List<String> key = List.of(normalizedWord1, normalizedWord2);
        int[] counter = state.statistics.get(key);
        if (counter != null) {
            counter[0]++;
        } else {
            state.statistics.put(key, new int[] { 1 });
        }
    }
    
    private List<ResultEntry> extractResult(int resultLimit) {
        State state = stateHolder.get();

        NavigableSet<ResultEntry> topEntries = new TreeSet<>(this::compareEntries);
        
        int index = 0;
        for (Map.Entry<List<String>, int[]> entry : state.statistics.entrySet()) {
            List<String> key = entry.getKey();
            String word1 = key.get(0);
            String word2 = key.get(1);
            int count = entry.getValue()[0];

            if (index < resultLimit) {
                topEntries.add(new ResultEntry(word1, word2, count));
            } else if (count > topEntries.last().count) {
                topEntries.pollLast();
                topEntries.add(new ResultEntry(word1, word2, count));
            }
            
            index++;
        }

        return new ArrayList<>(topEntries);
    }
    
    private int compareEntries(ResultEntry entry1, ResultEntry entry2) {
        // count is sorted in reverse order (largest is the first)
        int cmp = Integer.compare(entry2.count, entry1.count);
        if (cmp != 0) {
            return cmp;
        }
        
        cmp = collator.compare(entry1.word1, entry2.word1);
        if (cmp != 0) {
            return cmp;
        }

        return collator.compare(entry1.word2, entry2.word2);
    }
    
    
    public static class ResultEntry {
        
        private final String word1;
        
        private final String word2;
        
        private final int count;
        
        
        public ResultEntry(String word1, String word2, int count) {
            this.word1 = word1;
            this.word2 = word2;
            this.count = count;
        }


        public String word1() {
            return word1;
        }

        public String word2() {
            return word2;
        }

        public int count() {
            return count;
        }

        @Override
        public String toString() {
            return String.format("%s %s: %d", word1, word2, count);
        }


        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(count)
                    .append(word1)
                    .append(word2)
                    .toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ResultEntry)) {
                return false;
            }
            
            ResultEntry otherEntry = (ResultEntry) other;
            
            return
                    word1.equals(otherEntry.word1) &&
                    word2.equals(otherEntry.word2) &&
                    count == otherEntry.count;
        }
        
    }
    
    
    private static class State {
        
        private CharType previousCharType = CharType.OTHER;
        
        private String previousWord = null;
        
        private StringBuilder currentWordBuilder = new StringBuilder();
        
        private Map<List<String>, int[]> statistics = new HashMap<>();
        
    }
    
}
