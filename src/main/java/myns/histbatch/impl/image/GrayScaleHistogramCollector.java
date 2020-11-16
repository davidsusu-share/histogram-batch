package myns.histbatch.impl.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import myns.histbatch.api.image.GrayScaleExtractor;

public class GrayScaleHistogramCollector {

    private final GrayScaleExtractor grayScaleExtractor;
    
    
    public GrayScaleHistogramCollector(GrayScaleExtractor grayScaleExtractor) {
        this.grayScaleExtractor = Objects.requireNonNull(grayScaleExtractor);
    }
    
    
    public int collect(BufferedImage image, int[] destData) {
        int width = image.getWidth();
        int height = image.getHeight();

        int max = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                int grey = grayScaleExtractor.extractFrom(color);
                destData[grey]++;
                if (destData[grey] > max) {
                    max = destData[grey];
                }
            }
        }
        
        return max;
    }
    
}
