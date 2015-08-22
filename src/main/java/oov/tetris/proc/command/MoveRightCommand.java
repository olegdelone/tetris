package oov.tetris.proc.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;
import oov.tetris.util.Logger;

/**
 * Created by olegdelone on 22.08.2015.
 */
public class MoveRightCommand implements CtrlCommand {
    private static transient Logger log = Logger.getLogger(MoveRightCommand.class);

    private final BitsPool bitsPool;
    private final CompoundObj compoundObj;
    private final int cx;

    public MoveRightCommand(BitsPool bitsPool, CompoundObj compoundObj, int cx) {
        this.bitsPool = bitsPool;
        this.compoundObj = compoundObj;
        this.cx = cx;
    }

    @Override
    public void execute() {
        BoxPoint cursor = compoundObj.getCursor();
        int cx = cursor.getX();
        int cy = cursor.getY();
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        if (cx < this.cx - 1) {
            if (bitsPool.checkGapsClash(xGap, yGap, cx+1, cy)) {
//                log.debug("gaps clashed");
                CompoundObj cloned;
                try {
                    cloned = compoundObj.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                cloned.moveRight();
                if (!bitsPool.checkInPool(cloned)) {
                    compoundObj.moveRight();
                }
            } else {
                compoundObj.moveRight();
            }
        }
    }
}
