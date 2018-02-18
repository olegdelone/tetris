package oov.tetris.draw.menu;


import oov.tetris.draw.item.CompoundObj;

import java.awt.*;


public class CellsPlayground extends Cells {

    public CellsPlayground(int xCapacity, int yCapacity, int w, int h, Color borderColor) {
        super(xCapacity, yCapacity, w, h, borderColor);
    }

    @Override
    public void addNextCurrentObject(CompoundObj compoundObj) {
        super.addNextCurrentObject(compoundObj);
        compoundObj.moveTo(xCapacity >> 1, 0);
    }
}
