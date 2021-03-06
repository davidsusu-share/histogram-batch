package myns.histbatch.processor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import javax.imageio.ImageIO;

import myns.histbatch.image.GrayScaleHistogramCollector;

/**
 * {@link ContentProcessor} implementation for producing a gray scale histogram image
 */
public class ImageGrayScaleHistogramContentProcessor implements ContentProcessor {
    
    private static final String DEFAULT_OUT_FORMAT_NAME = "bmp";
    
    private static final int LENGTH = 256;
    
    private static final int HEIGHT = 100;
    
    
    private final GrayScaleHistogramCollector grayScaleHistogramCollector;
    
    private final String outFormatName;
    

    public ImageGrayScaleHistogramContentProcessor(
            GrayScaleHistogramCollector grayScaleHistogramCollector) {
        
        this(grayScaleHistogramCollector, DEFAULT_OUT_FORMAT_NAME);
    }

    public ImageGrayScaleHistogramContentProcessor(
            GrayScaleHistogramCollector grayScaleHistogramCollector,
            String formatName) {

        this.grayScaleHistogramCollector = Objects.requireNonNull(grayScaleHistogramCollector);
        this.outFormatName = Objects.requireNonNull(formatName);
    }
    

    @Override
    public void process(InputStream in, OutputStream out) throws IOException {
        int[] data = new int[LENGTH];
        int max = grayScaleHistogramCollector.collect(ImageIO.read(in), data);
        RenderedImage histogramImage = drawHistogram(data, max);
        ImageIO.write(histogramImage, outFormatName, out);
    }
    
    private RenderedImage drawHistogram(int[] data, int max) {
        BufferedImage image = new BufferedImage(LENGTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = image.createGraphics();
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, LENGTH, HEIGHT);

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
