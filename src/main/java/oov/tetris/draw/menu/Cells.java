package oov.tetris.draw.menu;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.Drawable;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.RenderEngine;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Olezha
 * Date: 08.08.14
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class Cells extends Drawable implements TetrisControl {
    private final int cellW;
    private final int cellH;
    protected final int xCapacity;
    private final int yCapacity;
    private final int w;
    private final int h;
    private final Color borderColor;

    public int getCellW() {
        return cellW;
    }

    public int getCellH() {
        return cellH;
    }

    public Cells(int xCapacity, int yCapacity, int w, int h, Color borderColor) {
        this.cellW = w/xCapacity;
        this.cellH = h/yCapacity;
        this.w = w;
        this.h = h;
        this.xCapacity = xCapacity;
        this.yCapacity = yCapacity;
        this.borderColor = borderColor;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void addNextCurrentObject(CompoundObj compoundObj){
        for (BoxPoint boxPoint : compoundObj.getBoxPoints()) {
            boxPoint.setTetrisControl(this);
            RenderEngine.getInstance().add(boxPoint);
        }
        compoundObj.getCursor().setTetrisControl(this);
    }



    @Override
    public void draw(Graphics g, int sx, int sy) {
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
    public Point getShiftPoint(Drawable drawable) {

        return tetrisControl.getShiftPoint(this);
    }


}
