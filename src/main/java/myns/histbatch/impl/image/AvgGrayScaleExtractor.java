package myns.histbatch.impl.image;

import java.awt.Color;

import myns.histbatch.api.image.GrayScaleExtractor;

public class AvgGrayScaleExtractor implements GrayScaleExtractor {

    @Override
    public int extractFrom(Color color) {
        int sum = color.getRed() + color.getGreen() + color.getBlue();
        double avg = sum / 3f;
        return (int) Math.round(avg);
    }

}
