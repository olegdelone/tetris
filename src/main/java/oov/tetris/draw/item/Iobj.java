package oov.tetris.draw.item;

import oov.tetris.draw.BoxPoint;

import java.awt.*;


public class Iobj extends CompoundObj {

    public Iobj(int x, int y, Color color, int cellW, int cellH) {
        super(x, y, color, cellW, cellH);
    }

    @Override
    protected BoxPoint[] obtainFigure(int x, int y, Color color, int cellW, int cellH) {
        BoxPoint d;
        return new BoxPoint[]{d = BoxPoint.makeBoxPoint(x, y, color, cellW, cellH),
                d = BoxPoint.makeBoxUpper(d.getX(), d.getY(), color, cellW, cellH),
                d = BoxPoint.makeBoxUpper(d.getX(), d.getY(), color, cellW, cellH),
                BoxPoint.makeBoxUpper(d.getX(), d.getY(), color, cellW, cellH),
        };
    }

    @Override
    public void rotateCCW() {
        if (isFlat()) {
            super.rotateCCW();
            moveLeft();
        } else {
            super.rotateCW();
            moveRight();
        }
    }

    @Override
    public void rotateCW() {
        rotateCCW();
    }

    @Override
    public int getOrigCPX() {
        return super.getOrigCPX() + (!isFlat() ? 1 : 0) ;
    }
}
