package oov.tetris.draw.item;

import oov.tetris.draw.BoxPoint;
import oov.tetris.manager.ChunksFactory;

import java.awt.*;

/**
 * Created by Olegdelone on 23.07.2015.
 */
public class Jobj extends CompoundObj{

    public Jobj(int x, int y, Color color, int cellW, int cellH) {
        super(x, y, color, cellW, cellH);
        BoxPoint d;
        boxPoints = new BoxPoint[]{d = BoxPoint.makeBoxPoint(x, y, color, cellW, cellH),
                d = BoxPoint.makeBoxRighter(d.getX(), d.getY(), color, cellW, cellH),
                d = BoxPoint.makeBoxUpper(d.getX(), d.getY(), color, cellW, cellH),
                BoxPoint.makeBoxUpper(d.getX(), d.getY(), color, cellW, cellH),
        };
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.setGameContainer(this);
        }
    }
}