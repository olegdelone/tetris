package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class BPool1 extends BitesPool {
    private static Logger log = LoggerFactory.getLogger(BPool1.class);

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
        Map<Integer, BoxPoint> boxPoints = rowMap.get(boxPoint.getY());
        if (boxPoints == null) {
//            boxPoints = new LinkedList<BoxPoint>();
            boxPoints = new MyMap(xCap);
            rowMap.put(boxPoint.getY(), boxPoints);
        }
        boxPoints.put(boxPoint.getX(), boxPoint);
        super.put(boxPoint);
    }

    @Override
    public void moveTopPartDown(int fromYIndex, int stepsUp) {
        log.info("fromY: {}, stepsDown: {}", fromYIndex, stepsUp);
        m.put(fromYIndex, stepsUp);
        super.moveTopPartDown(fromYIndex, stepsUp);
        log.warn("pool : {}", this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BitesPool{");
        sb.append("\n");
        sb.append("\n---______>");
        for (int i = 0; i < xCap; sb.append("(").append(i++).append(")")) ;
        sb.append("\n");
        for (int j = 0; j < yCap; j++) {
            Map<Integer, BoxPoint> boxPoints = rowMap.get(j);
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
