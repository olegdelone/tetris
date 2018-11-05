package oov.tetris.draw.controller.command;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.BitesPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MoveDownCommand implements CtrlCommand {
    private static Logger log = LoggerFactory.getLogger(MoveDownCommand.class);

    private final BitesPool bitesPool;
    private final CompoundObj compoundObj;
    private final int cy;
    private final ObjPutListener objPutListener;

    public MoveDownCommand(BitesPool bitesPool, CompoundObj compoundObj, int cy, ObjPutListener objPutListener) {
        this.bitesPool = bitesPool;
        this.compoundObj = compoundObj;
        this.cy = cy;
        this.objPutListener = objPutListener;
    }

    @Override
    public void execute() {
        BoxPoint cursor = compoundObj.getCursor();
        if (cursor.getY() == cy - 1) {
            objPutListener.onEvent(compoundObj);
        } else if (bitesPool.checkNextYInPool(compoundObj)) {
            log.debug("clash detected");
            CompoundObj cloned;
            try {
                cloned = compoundObj.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            cloned.moveDown();
            if(bitesPool.checkInPool(cloned)){
                objPutListener.onEvent(compoundObj);
            } else {
                objPutListener.onEvent(cloned);
            }
        } else {
            compoundObj.moveDown();
        }
    }

    public interface ObjPutListener {
        void onEvent(CompoundObj compoundObj);
    }
}
