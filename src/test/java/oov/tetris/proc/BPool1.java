package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.util.Logger;

import java.util.HashMap;
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
    public void put(BoxPoint boxPoint) {
        Map<Integer, BoxPoint> boxPoints = baskets.get(boxPoint.getY());
        if (boxPoints == null) {
//            boxPoints = new LinkedList<BoxPoint>();
            boxPoints = new MyMap(xCap);
            baskets.put(boxPoint.getY(), boxPoints);
        }
        boxPoints.put(boxPoint.getX(), boxPoint);
        super.put(boxPoint);
    }

    @Override
    void moveTopPartDown(int fromY, int stepsDown) {
        log.info("fromY: {}, stepsDown: {}",  fromY, stepsDown);
        m.put(fromY, stepsDown);
        super.moveTopPartDown(fromY, stepsDown);
        log.warn("pool : {}", this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BitsPool{");
        sb.append("\n");
        sb.append("\n---______>");
        for (int i = 0; i < xCap; sb.append("(").append(i++).append(")")) ;
        sb.append("\n");
        for (int j = 0; j < yCap; j++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(j);
            sb.append("(").append(j).append(") ");
            if(boxPoints != null){

                sb.append(boxPoints.toString());
            } else {
                sb.append("null ");
            }
            for (int i = 0; i < xCap; i++) {
                BoxPoint boxPoint = null;
                if (boxPoints != null) {
                    boxPoint = boxPoints.get(i);
                }
                sb.append("[").append(boxPoint == null ? "-" : "X").append("]");

            }
            sb.append("\n");
        }
        return sb.append("}").toString();
    }
}
