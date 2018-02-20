package oov.tetris;

import oov.tetris.draw.Drawable;
import oov.tetris.proc.RenderEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;


public class ImageComponent extends JComponent implements RenderEngine.RenderListener {

    private final BufferedImage image;

    private final int w;
    private final int h;

    public ImageComponent(int w, int h) {
        this.w = w;
        this.h = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Dimension dimension = new Dimension(w, h);

        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        this.setPreferredSize(dimension);
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    private void reset() {
        Color bgColor = Color.BLACK;
        Graphics gr = image.getGraphics();
        gr.setColor(bgColor);
        gr.fillRect(0, 0, w, h);
    }

    @Override
    public void onEvent(Collection<Drawable> drawables) {
        reset();
        Graphics graphics = image.getGraphics();
        synchronized (drawables) {
            for (Drawable drawable : drawables) {
                drawable.draw(graphics);
            }
        }
        render();
    }

    private void render() {
        this.getGraphics().drawImage(image, 0, 0, w, h, 0, 0, w, h, null);
    }
}