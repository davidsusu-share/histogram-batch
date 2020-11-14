package myns.histbatch.impl.image;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import myns.histbatch.api.image.GrayScaleExtractor;

class LuminancePreservingGrayScaleExtractorTest {

    @Test
    void testResults() {
        GrayScaleExtractor extractor = new LuminancePreservingGrayScaleExtractor();
        
        List<Color> colors = List.of(
                new Color(0, 0, 0),
                new Color(100, 100, 100),
                new Color(255, 255, 255),
                new Color(100, 100, 101),
                new Color(255, 200, 200),
                new Color(100, 150, 200),
                new Color(223, 147, 69),
                new Color(65, 221, 89),
                new Color(173, 36, 198),
                new Color(32, 210, 213),
                new Color(45, 47, 41),
                new Color(173, 183, 93));
        
        List<Integer> actual = colors.stream()
                .map(extractor::extractFrom)
                .collect(Collectors.toList());
        
        List<Integer> expected = List.of(
                0, 168, 255, 168, 235, 197, 206, 218, 149, 214, 118, 216);
        
        assertThat(actual).isEqualTo(expected);
    }

}
