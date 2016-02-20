package oov.tetris.proc.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.ObjPutListener;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;
import oov.tetris.util.Logger;

/**
 * Created by olegdelone on 22.08.2015.
 */
public class MoveLeftCommand implements CtrlCommand {
    private static transient Logger log = Logger.getLogger(MoveLeftCommand.class);

    private final BitsPool bitsPool;
    private final CompoundObj compoundObj;

    public MoveLeftCommand(BitsPool bitsPool, CompoundObj compoundObj) {
        this.bitsPool = bitsPool;
        this.compoundObj = compoundObj;
    }

    @Override
    public void execute() {
        BoxPoint cursor = compoundObj.getCursor();
        int cx = cursor.getX();
        int cy = cursor.getY();
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        if (cx - xGap > 0) {
            if (bitsPool.checkGapsClash(xGap, yGap, cx-1, cy)) {
//                log.debug("gaps clashed");
                CompoundObj cloned;
                try {
                    cloned = compoundObj.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                cloned.moveLeft();
                if (!bitsPool.checkInPool(cloned)) {
                    compoundObj.moveLeft();
                }
            } else {
                compoundObj.moveLeft();
            }
        }
    }
}
