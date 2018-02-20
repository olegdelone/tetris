package oov.tetris.draw.controller.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MoveLeftCommand implements CtrlCommand {
    private static Logger log = LoggerFactory.getLogger(MoveLeftCommand.class);

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
                cloned.moveLeft(1);
                if (!bitsPool.checkInPool(cloned)) {
                    compoundObj.moveLeft();
                }
            } else {
                compoundObj.moveLeft();
            }
        }
    }
}
