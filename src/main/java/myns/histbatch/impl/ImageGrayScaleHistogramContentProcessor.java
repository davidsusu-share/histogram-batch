package myns.histbatch.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import myns.histbatch.api.ContentProcessor;
import myns.histbatch.api.image.GrayScaleExtractor;

public class ImageGrayScaleHistogramContentProcessor implements ContentProcessor {
    
    private static final int LENGTH = 256;
    
    private static final int HEIGHT = 100;
    
    
    private final GrayScaleExtractor grayScaleExtractor;
    
    private final String formatName;
    
    
    public ImageGrayScaleHistogramContentProcessor(
            GrayScaleExtractor grayScaleExtractor,
            String formatName) {

        this.grayScaleExtractor = grayScaleExtractor;
        this.formatName = formatName;
    }
    

    @Override
    public void process(InputStream in, OutputStream out) throws IOException {
        int[] data = new int[LENGTH];
        int max = collectData(ImageIO.read(in), data);
        RenderedImage histogramImage = drawHistogram(data, max);
        ImageIO.write(histogramImage, formatName, out);
    }
    
    private int collectData(BufferedImage image, int[] destData) {
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
    
    private RenderedImage drawHistogram(int[] data, int max) {
        BufferedImage image = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = image.createGraphics();
        
        graphics.setColor(Color.WHITE);
        graphics.drawRect(0, 0, LENGTH, HEIGHT);

        graphics.setColor(Color.BLACK);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        int range = max > 0 ? max : 1;
        
        for (int i = 0; i < LENGTH; i++) {
            int value = data[i];
            int normalizedValue = (int) Math.round(((double) value) / range * HEIGHT);
            if (value > 0) {
                graphics.drawLine(i, HEIGHT - normalizedValue, i, HEIGHT - 1);
            }
        }
        
        return image;
        
    }

}
