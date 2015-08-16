package oov.tetris.proc;

import oov.tetris.util.Logger;

import java.util.Map;

/**
 * Created by Olegdelone on 16.08.2015.
 */
public class BPool1 extends BitsPool {
    private static transient Logger log = Logger.getLogger(BPool1.class);

    public Map<Integer, Integer> getM() {
        return m;
    }

    public void setM(Map<Integer, Integer> m) {
        this.m = m;
    }

    Map<Integer, Integer> m;

    public BPool1(int xCap, int yCap) {
        super(xCap, yCap);
    }

    @Override
    void moveTopPartDown(int fromY, int stepsDown) {
        log.info("fromY: {}, stepsDown: {}",  fromY, stepsDown);
        m.put(fromY, stepsDown);
    }
}
