package myns.histbatch.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Algorithm for collecting gray value statistics from an image
 */
public class GrayScaleHistogramCollector {

    private final GrayScaleExtractor grayScaleExtractor;
    
    
    /**
     * Creates a collector with the given {@link GrayScaleExtractor}
     * 
     * @param grayScaleExtractor The gray calculation strategy
     */
    public GrayScaleHistogramCollector(GrayScaleExtractor grayScaleExtractor) {
        this.grayScaleExtractor = Objects.requireNonNull(grayScaleExtractor);
    }
    

    /**
     * Collects the statistics of grey values in the given source image.
     * 
     * <code>destData</code> must be a 256-length integer array.
     * Each array index represents a gray value.
     * Each array value will be set to the number of occurrences of this gray value.
     * 
     * @param image Source image
     * @param destData Statistics of grey values
     * @return The maximum value in <code>destData</code>
     */
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
