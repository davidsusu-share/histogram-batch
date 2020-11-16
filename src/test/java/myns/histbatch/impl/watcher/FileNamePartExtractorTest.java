package myns.histbatch.impl.watcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;

class FileNamePartExtractorTest {

    @Test
    void testWithExtension() {
        File file = new File("/some/path/hello.txt");
        
        String actual = new FileNamePartExtractor().extract(file);
        String expected = "hello";
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testWithInnerDotAndExtension() {
        File file = new File("/some/path/lorem.ipsum.jpeg");
        
        String actual = new FileNamePartExtractor().extract(file);
        String expected = "lorem.ipsum";
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testHidden() {
        File file = new File("/some/path/.hidden");
        
        String actual = new FileNamePartExtractor().extract(file);
        String expected = "hidden";
        
        assertThat(actual).isEqualTo(expected);
    }

}
