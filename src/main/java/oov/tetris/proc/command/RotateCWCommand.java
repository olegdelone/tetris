package oov.tetris.proc.command;

import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;

/**
 * Created by olegdelone on 20.08.2015.
 */
public class RotateCWCommand extends AbsRotateCommand {
    public RotateCWCommand(BitsPool bitsPool, CompoundObj compoundObj, int cx) {
        super(bitsPool, compoundObj, cx);
    }

    @Override
    protected void originalAction(CompoundObj compoundObj) {
        compoundObj.rotateCW();
    }
}
