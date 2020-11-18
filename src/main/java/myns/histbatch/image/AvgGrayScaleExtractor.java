package myns.histbatch.image;

import java.awt.Color;

public class AvgGrayScaleExtractor implements GrayScaleExtractor {

    @Override
    public int extractFrom(Color color) {
        int sum = color.getRed() + color.getGreen() + color.getBlue();
        double avg = sum / 3f;
        return (int) Math.round(avg);
    }

}
