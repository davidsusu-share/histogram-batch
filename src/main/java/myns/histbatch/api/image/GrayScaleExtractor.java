package myns.histbatch.api.image;

import java.awt.Color;

@FunctionalInterface
public interface GrayScaleExtractor {

    public int extractFrom(Color color);
    
}
