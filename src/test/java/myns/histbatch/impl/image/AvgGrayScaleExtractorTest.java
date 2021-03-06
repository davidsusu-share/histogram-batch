package myns.histbatch.impl.image;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import myns.histbatch.image.AvgGrayScaleExtractor;
import myns.histbatch.image.GrayScaleExtractor;

class AvgGrayScaleExtractorTest {

    @Test
    void testResults() {
        GrayScaleExtractor extractor = new AvgGrayScaleExtractor();
        
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
                0, 100, 255, 100, 218, 150, 146, 125, 136, 152, 44, 150);
        
        assertThat(actual).isEqualTo(expected);
    }

}
