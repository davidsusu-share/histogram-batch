package myns.histbatch.image;

import java.awt.Color;

/**
 * Strategy interface for converting a color to a gray value
 */
@FunctionalInterface
public interface GrayScaleExtractor {

    /**
     * Calculates the gray value for the given {@link Color}
     * 
     * @param color Input color
     * @return The gray value of the color
     */
    public int extractFrom(Color color);
    
}
