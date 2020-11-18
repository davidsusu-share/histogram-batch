package myns.histbatch.impl.image;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import myns.histbatch.image.AvgGrayScaleExtractor;
import myns.histbatch.image.GrayScaleHistogramCollector;

class GrayScaleHistogramCollectorTest {

    @Test
    void testCraftedImage() {
        GrayScaleHistogramCollector collector = new GrayScaleHistogramCollector(
                new AvgGrayScaleExtractor());
        
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(new Color(240, 250, 210));
        graphics.fillRect(0, 0, 10, 10);
        
        graphics.setColor(new Color(230, 240, 35));
        graphics.drawLine(1, 1, 8, 8);

        graphics.setColor(new Color(255, 10, 10));
        graphics.fillRect(4, 1, 5, 5);

        graphics.setColor(new Color(30, 40, 150));
        graphics.drawLine(6, 2, 6, 8);

        graphics.setColor(new Color(80, 190, 10));
        graphics.fillRect(1, 6, 2, 2);

        graphics.setColor(new Color(70, 5, 25));
        graphics.fillRect(3, 9, 1, 1);

        int[] actualData = new int[256];
        int actualMax = collector.collect(image, actualData);

        int expectedMax = 62;
        int[] expectedData = new int[256];
        expectedData[33] = 1;
        expectedData[73] = 7;
        expectedData[92] = 21;
        expectedData[93] = 4;
        expectedData[168] = 5;
        expectedData[233] = 62;

        assertThat(actualMax).isEqualTo(expectedMax);
        assertThat(actualData).containsExactly(expectedData);
    }

}
