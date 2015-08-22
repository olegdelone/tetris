package oov.tetris.draw.item;

import oov.tetris.draw.BoxPoint;

import java.awt.*;

/**
 * Created by Olegdelone on 23.07.2015.
 */
public class Oobj extends CompoundObj{

    public Oobj(int x, int y, Color color, int cellW, int cellH) {
        super(x, y, color, cellW, cellH);
    }

    @Override
    protected BoxPoint[] obtainFigure(int x, int y, Color color, int cellW, int cellH) {
        BoxPoint d;
        return new BoxPoint[]{d = BoxPoint.makeBoxPoint(x, y, color,cellW, cellH),
                d = BoxPoint.makeBoxUpper(d.getX(), d.getY(), color,cellW, cellH),
                d = BoxPoint.makeBoxLefter(d.getX(),d.getY(), color,cellW, cellH),
                BoxPoint.makeBoxDowner(d.getX(), d.getY(), color,cellW, cellH),
        };
    }


    @Override
    public void rotateCW() {

    }

    @Override
    public void rotateCCW() {

    }
}
