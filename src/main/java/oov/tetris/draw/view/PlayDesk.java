package oov.tetris.draw.view;

import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.FiguresStack;
import oov.tetris.proc.RenderEngine;

import java.awt.*;


public class PlayDesk extends AncorControl implements FiguresStack.ChunksStackManagerCurrentListener {
    private final int cellW;
    private final int cellH;
    final int xCapacity;
    final int yCapacity;
    private final int w;
    private final int h;
    private final Color borderColor;

    public PlayDesk(int xCapacity, int yCapacity, int w, int h, Color borderColor) {
        this.cellW = w/xCapacity;
        this.cellH = h/yCapacity;
        this.w = w;
        this.h = h;
        this.xCapacity = xCapacity;
        this.yCapacity = yCapacity;
        this.borderColor = borderColor;
    }

    public PlayDesk(int cellW, int cellH, Color borderColor, int xCapacity, int yCapacity) {
        this.w = xCapacity*cellW;
        this.h = yCapacity*cellH;
        this.xCapacity = xCapacity;
        this.yCapacity = yCapacity;
        this.borderColor = borderColor;
        this.cellW = cellW;
        this.cellH = cellH;
    }

    public void placeObj(CompoundObj compoundObj){
        compoundObj.setAncor(getAncor());
        RenderEngine.getInstance().add(compoundObj);
        postShift(compoundObj);
    }

    @Override
    public void draw(Graphics g) {
        int sx = getAncor().x;
        int sy = getAncor().y;
        g.setColor(borderColor);
        int yMax = cellH * yCapacity;
        int xMax = cellW * xCapacity;
        for (int i = 0; i <= xCapacity; i++) {
            g.drawLine(sx + (i) * cellW, sy, sx + (i) * cellW, yMax + sy);
            if (i == 0) {
                g.drawLine(sx + 1, sy, sx + 1, yMax + sy);
            } else if (i == xCapacity) {
                g.drawLine(sx - 1 + xMax, sy, sx - 1 + xMax, yMax + sy);
            }
        }
        for (int i = 0; i <= yCapacity; i++) {
            g.drawLine(sx, sy + (i) * cellH, xMax + sx, sy + (i) * cellH);
            if (i == 0) {
                g.drawLine(sx, sy + 1, xMax + sx, sy + 1);
            } else if (i == yCapacity) {
                g.drawLine(sx, sy + yMax - 1, xMax + sx, sy + yMax - 1);
            }
        }
    }

    @Override
    public void onObjIsReady(CompoundObj current) {
        placeObj(current);
    }

    public int getCellW() {
        return cellW;
    }
    public int getCellH() {
        return cellH;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }

    protected void postShift(CompoundObj compoundObj){
        compoundObj.moveTo(xCapacity >> 1, 0);
    }
}
