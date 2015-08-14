package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.util.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Olegdelone on 05.08.2015.
 */
public class BitsPool {
    private final Map<Integer, Map<Integer,BoxPoint>> baskets;
    private int xCap;
    private int yCap;
//    private Color color = Color.LIGHT_GRAY;

//    private static BitsPool bitsPool = new BitsPool();
//
//    public static BitsPool getInstance(){
//        return bitsPool;
//    }

    public BitsPool(int xCap, int yCap) {
        this.baskets = new HashMap<Integer, Map<Integer,BoxPoint>>(yCap);
        this.xCap = xCap;
        this.yCap = yCap;
    }

    public void put(CompoundObj compoundObj) {
        //sync??

        for (BoxPoint boxPoint : compoundObj.getBoxPoints()) {
//            boxPoint.setInnerColor(color);
            put(boxPoint);
        }
        compoundObj.dispose();

    }

    public void put(BoxPoint boxPoint) {
        Map<Integer,BoxPoint> boxPoints = baskets.get(boxPoint.getY());
        if (boxPoints == null) {
//            boxPoints = new LinkedList<BoxPoint>();
            boxPoints = new HashMap<Integer,BoxPoint>(xCap);
            baskets.put(boxPoint.getY(), boxPoints);
        }
        boxPoints.put(boxPoint.getX(), boxPoint);
    }

    private void moveTopPartDown(int fromY, int stepsDown) {
        Map<Integer,BoxPoint>[] tmpTop = new Map[stepsDown];
        for (int y = fromY, i = 0; y < fromY + stepsDown; y++, tmpTop[i] = baskets.get(i++)) ;

        for (Map.Entry<Integer, Map<Integer,BoxPoint>> entry : baskets.entrySet()) {
            Map<Integer,BoxPoint> boxPoints = entry.getValue();
            int entryY = entry.getKey();
            if (boxPoints == null || boxPoints.isEmpty()) {
                break;
            }

            if (entryY < fromY) {
                for (BoxPoint boxPoint : boxPoints.values()) {
                    boxPoint.addY(stepsDown);
                }
                baskets.put(entryY + stepsDown, boxPoints);
            }
        }
        for (int i = 0; i < tmpTop.length; i++) {
            baskets.put(i, tmpTop[i]);
        }
    }
//    private void moveTopPartDown(int fromY, int stepsDown) {
//        List<BoxPoint> bp = new ArrayList<BoxPoint>();
//        for (Map.Entry<Integer, List<BoxPoint>> entry : baskets.entrySet()) {
//            List<BoxPoint> boxPoints = entry.getValue();
//            int entryY = entry.getKey();
//            if (boxPoints == null || boxPoints.isEmpty()) {
//                break;
//            }
//            if (entryY <= fromY) {
//                for (BoxPoint boxPoint : boxPoints) {
//                    boxPoint.addY(stepsDown);
//                    bp.add(boxPoint);
//                }
//                boxPoints.clear();
//            }
//        }
//        for (BoxPoint boxPoint : bp) {
//            put(boxPoint);
//        }
////        for (int y = fromY, i=0; y < fromY + stepsDown; y++) {
////            List<BoxPoint> tmp = baskets.get(i);
////            baskets.put()
////        }
//    }

    private List<Integer> checkLines() {
        List<Integer> r = Collections.emptyList();
        for (Map.Entry<Integer, Map<Integer,BoxPoint>> entry : baskets.entrySet()) {
            Map<Integer,BoxPoint> boxPoints = entry.getValue();
            if (xCap == boxPoints.size()) {
                if (r == Collections.EMPTY_LIST) {
                    r = new ArrayList<Integer>(2);
                }
                r.add(entry.getKey());
            }
        }
        return r;
    }

    public List<Integer> eraseLines() {
        List<Integer> r = checkLines();
        if (r == Collections.EMPTY_LIST) {
            return r;
        }

        int size = r.size();
        int bunch = 1;
        Integer cur, prev = null;
//        cur = prev = r.get(size-1);
        for (int i = size - 1; i >= 0; i--) {
            cur = r.get(i);
            Map<Integer,BoxPoint> boxPoints = baskets.get(cur);
            RenderEngine.getInstance().removeAll(boxPoints.values()); // animation
            boxPoints.clear();
            if (i == size) {
                moveTopPartDown(cur, bunch);
            } else if ((prev != null)) {
                if (prev - cur == 1) {
                    bunch++;
                } else {
                    moveTopPartDown(cur, bunch);
                    bunch = 1;
                }
            } else {
                prev = cur;
            }
        }

        for (Integer integer : r) {
//            int steps = r.indexOf(integer);
            Map<Integer,BoxPoint> boxPoints = baskets.get(integer);
            RenderEngine.getInstance().removeAll(boxPoints.values()); // animation
            boxPoints.clear();

        }
//        for (Integer integer : r) {
//            moveTopPartDown(integer, ); // animation
//        }
        return r;
    }

    public boolean checkInPool(CompoundObj compoundObj) {
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        int x = compoundObj.getCursor().getX();
        int y = compoundObj.getCursor().getY();
        for (int i = y - yGap; i <= y; i++) {
            Map<Integer,BoxPoint> boxPoints = baskets.get(i);
            for (int j = x; j <= x + xGap; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    for (BoxPoint point : compoundObj.getBoxPoints()) {
                        if (boxPoint.equals(point)) {
                            point.setInnerColor(Color.WHITE);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkNextYInPool(CompoundObj compoundObj) {
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        int x = compoundObj.getCursor().getX();
        int y = compoundObj.getCursor().getY() + 1;
        for (int i = y - yGap; i < y; i++) {
            Map<Integer,BoxPoint> boxPoints = baskets.get(i);
            for (int j = x; j < x + xGap; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    for (BoxPoint point : compoundObj.getBoxPoints()) {
                        if (boxPoint.getX() == point.getX() && boxPoint.getY() == point.getY() + 1) {
                            point.setInnerColor(Color.WHITE);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private static transient Logger log = Logger.getLogger(BitsPool.class);

    public boolean checkGapsClash(int xGap, int yGap, int x, int y) {
        for (int i = y - yGap; i <= y; i++) {
            Map<Integer,BoxPoint> boxPoints = baskets.get(i);
            for (int j = x; j <= x + xGap; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    log.debug("boxPoint: {}", boxPoint);
                    boxPoint.setInnerColor(Color.WHITE);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BitsPool{");
        sb.append("\n");
        sb.append("\n--->");
        for (int i = 0; i < xCap; sb.append("(").append(i++).append(")"));
        sb.append("\n");
        for (int j = 0; j < yCap; j++) {
            sb.append("(").append(j).append(") ");
            Map<Integer,BoxPoint> boxPoints = baskets.get(j);
            for (int i = 0; i < xCap; i++) {
                BoxPoint boxPoint = null;
                if(boxPoints != null){
                   boxPoint = boxPoints.get(i);
                }
                sb.append("[").append(boxPoint == null ? "-" : "X").append("]");

            }
            sb.append("\n");
        }
        return sb.append("}").toString();
    }

    public boolean checkInPool(int x, int y){
        Map<Integer, BoxPoint> pointMap = baskets.get(y);
        return pointMap != null && pointMap.get(x) != null;
    }
}
