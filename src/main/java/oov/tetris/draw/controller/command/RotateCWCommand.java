package oov.tetris.draw.controller.command;

import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitesPool;

/**
 * Created by olegdelone on 20.08.2015.
 */
public class RotateCWCommand extends AbsRotateCommand {
    public RotateCWCommand(BitesPool bitesPool, CompoundObj compoundObj, int cx) {
        super(bitesPool, compoundObj, cx);
    }

    @Override
    protected void originalAction(CompoundObj compoundObj) {
        compoundObj.rotateCW();
    }
}
