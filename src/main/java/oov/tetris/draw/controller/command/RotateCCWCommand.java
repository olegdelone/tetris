package oov.tetris.draw.controller.command;

import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;

/**
 * Created by olegdelone on 20.08.2015.
 */
public class RotateCCWCommand extends AbsRotateCommand {
    public RotateCCWCommand(BitsPool bitsPool, CompoundObj compoundObj, int cx) {
        super(bitsPool, compoundObj, cx);
    }

    @Override
    protected void originalAction(CompoundObj compoundObj) {
        compoundObj.rotateCCW();
    }
}
