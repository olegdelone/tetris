package oov.tetris.draw.view;

import oov.tetris.draw.Drawable;
import oov.tetris.proc.RenderEngine;

import java.awt.*;
import java.text.DecimalFormat;

public class FpsText implements Drawable, RenderEngine.FpsListener {
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.00");

    private float fps;

    public void setFps(float fps) {
        this.fps = fps;
    }

    @Override
    public void draw(Graphics g) {
        g.drawString(String.valueOf(DECIMAL_FORMAT.format(fps)), 5, 10);
    }

    @Override
    public void onEvent(float fps) {
        setFps(fps);
    }
}

