package oov.tetris.draw.view;


import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.FiguresStack;

import java.awt.*;


public class PreviewDesk extends PlayDesk implements FiguresStack.ChunksStackManagerOngoingListener{

    public PreviewDesk(int xCapacity, int yCapacity, int w, int h, Color borderColor) {
        super(xCapacity, yCapacity, w, h, borderColor);
    }

    public PreviewDesk(int cellW, int cellH, Color borderColor, int xCapacity, int yCapacity) {
        super(cellW, cellH, borderColor, xCapacity, yCapacity);
    }

    @Override
    public void postShift(CompoundObj compoundObj) {
        int x = compoundObj.getxGap();
        int y = compoundObj.getyGap();
        int xShift = ((xCapacity - x) >> 1)+x;
        int yShift = ((yCapacity - y) >> 1) + y;
        compoundObj.moveTo(xShift, yShift);
    }

    @Override
    public void onOngoingObjIsReady(CompoundObj ongoing) {
        placeObj(ongoing);
    }

}
