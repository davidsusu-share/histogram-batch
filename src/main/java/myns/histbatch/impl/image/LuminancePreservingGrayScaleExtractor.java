package myns.histbatch.impl.image;

import java.awt.Color;

import myns.histbatch.api.image.GrayScaleExtractor;

public class LuminancePreservingGrayScaleExtractor implements GrayScaleExtractor {

    private static final double DATA_FACTOR = 255d;

    private static final double RED_WEIGHT = 0.2126;

    private static final double GREEN_WEIGHT = 0.7152;

    private static final double BLUE_WEIGHT = 0.0722;
    
    private static final double LIMIT = 0.0031308;
    
    private static final double LOW_FACTOR = 12.92;
    
    private static final double HIGH_DIFF = 0.055;
    
    private static final double HIGH_FACTOR = 1d + HIGH_DIFF;
    
    private static final double HIGH_POWER = 1d / 2.4;
    
    
    @Override
    public int extractFrom(Color color) {
        double red = color.getRed() / DATA_FACTOR;
        double green = color.getGreen() / DATA_FACTOR;
        double blue = color.getBlue() / DATA_FACTOR;
        
        double linearValue =
                (red * RED_WEIGHT) +
                (green * GREEN_WEIGHT) + 
                (blue * BLUE_WEIGHT);
        
        double srgbValue;
        if (linearValue > LIMIT) {
            srgbValue = (Math.pow(linearValue, HIGH_POWER) * HIGH_FACTOR) - HIGH_DIFF;
        } else {
            srgbValue = linearValue * LOW_FACTOR;
        }
        
        return (int) Math.round(srgbValue * DATA_FACTOR);
    }

}
