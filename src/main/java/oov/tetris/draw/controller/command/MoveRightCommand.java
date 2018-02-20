package oov.tetris.draw.controller.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitesPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MoveRightCommand implements CtrlCommand {
    private static Logger log = LoggerFactory.getLogger(MoveRightCommand.class);

    private final BitesPool bitesPool;
    private final CompoundObj compoundObj;
    private final int cx;

    public MoveRightCommand(BitesPool bitesPool, CompoundObj compoundObj, int cx) {
        this.bitesPool = bitesPool;
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
            if (bitesPool.checkGapsClash(xGap, yGap, cx+1, cy)) {
//                log.debug("gaps clashed");
                CompoundObj cloned;
                try {
                    cloned = compoundObj.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                cloned.moveRight();
                if (!bitesPool.checkInPool(cloned)) {
                    compoundObj.moveRight();
                }
            } else {
                compoundObj.moveRight();
            }
        }
    }
}
