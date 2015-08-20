package oov.tetris.proc.command;

import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;
import oov.tetris.util.Logger;

/**
 * Created by olegdelone on 20.08.2015.
 */
public abstract class AbsRotateCommand implements CtrlCommand {
    private static transient Logger log = Logger.getLogger(AbsRotateCommand.class);

    private BitsPool bitsPool;
    private CompoundObj compoundObj;
    private int cx;

    public AbsRotateCommand(BitsPool bitsPool, CompoundObj compoundObj, int cx) {
        this.bitsPool = bitsPool;
        this.compoundObj = compoundObj;
        this.cx = cx;
    }

    protected abstract void originalAction(CompoundObj compoundObj);

    @Override
    public void execute() {
        int rpx = compoundObj.getOrigRPX();
        int x_ = rpx + compoundObj.getyGap() - (cx - 1);
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
            if (!bitsPool.checkInPool(cloned)) {
                compoundObj.moveLeft(x_);
                originalAction(compoundObj);
//                originalAction(compoundObj);
            }

        } else if ((x_ = rpx) < 0) {
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
            if (!bitsPool.checkInPool(cloned)) {
                compoundObj.moveRight(x_);
                originalAction(compoundObj);
            }

        } else {
            originalAction(compoundObj);
        }
    }
}
