package oov.tetris.proc.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.ObjPutListener;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitsPool;
import oov.tetris.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olegdelone on 22.08.2015.
 */
public class MoveDownCommand implements CtrlCommand {
    private static transient Logger log = Logger.getLogger(MoveDownCommand.class);

    private final BitsPool bitsPool;
    private final CompoundObj compoundObj;
    private final int cy;
    private final ObjPutListener objPutListener;

    public MoveDownCommand(BitsPool bitsPool, CompoundObj compoundObj, int cy, ObjPutListener objPutListener) {
        this.bitsPool = bitsPool;
        this.compoundObj = compoundObj;
        this.cy = cy;
        this.objPutListener = objPutListener;
    }

    @Override
    public void execute() {
        BoxPoint cursor = compoundObj.getCursor();
        if (cursor.getY() == cy - 1) {
            bitsPool.put(compoundObj);
            objPutListener.onEvent(compoundObj);
        } else if (bitsPool.checkNextYInPool(compoundObj)) {
            log.debug("clash detected");
            CompoundObj cloned;
            try {
                cloned = compoundObj.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            cloned.moveDown();

            if(bitsPool.checkInPool(cloned)){
                bitsPool.put(compoundObj);
                objPutListener.onEvent(compoundObj);
            } else {
                bitsPool.put(cloned);
                objPutListener.onEvent(cloned);
            }
        } else {
            compoundObj.moveDown();
        }
    }
}
