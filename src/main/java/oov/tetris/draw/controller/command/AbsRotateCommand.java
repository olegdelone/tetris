package oov.tetris.draw.controller.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitesPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbsRotateCommand implements CtrlCommand {
    private static Logger log = LoggerFactory.getLogger(AbsRotateCommand.class);

    private final BitesPool bitesPool;
    private final CompoundObj compoundObj;
    private final int cx;

    public AbsRotateCommand(BitesPool bitesPool, CompoundObj compoundObj, int cx) {
        this.bitesPool = bitesPool;
        this.compoundObj = compoundObj;
        this.cx = cx;
    }

    protected abstract void originalAction(CompoundObj compoundObj);

    @Override
    public void execute() {
        int rpx = compoundObj.getOrigCPX();
        int x_ = rpx - (cx - 1);

        BoxPoint cursor = compoundObj.getCursor();
        int cx = cursor.getX();
        int cy = cursor.getY();
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();

        if (x_ > 0) {
            log.debug("x_: {}", x_);
            CompoundObj cloned;
            try {
                cloned = compoundObj.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            log.debug("cloned: {}", cloned);
            cloned.rotateCW();
            log.debug("cloned&rotated: {}", cloned);
            cloned.moveLeft(x_);
            log.debug("cloned&rotated&moved: {}", cloned);
            if (!bitesPool.checkInPool(cloned)) {
                compoundObj.moveLeft(x_);
                originalAction(compoundObj);
            }
        } else if ((x_ = rpx - yGap) < 0) {
            log.debug("x_: {}", x_);
            x_ = -x_;
            CompoundObj cloned;
            try {
                cloned = compoundObj.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            log.debug("cloned: {}", cloned);
            cloned.rotateCW();
            log.debug("cloned&rotated: {}", cloned);
            cloned.moveRight(x_);
            log.debug("cloned&rotated&moved: {}", cloned);
            if (!bitesPool.checkInPool(cloned)) {
                compoundObj.moveRight(x_);
                originalAction(compoundObj);
            }
        } else {
            if(!bitesPool.checkGapsClash(yGap,xGap,rpx,cy)){
                originalAction(compoundObj);
            }
        }
    }
}
