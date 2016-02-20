package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.util.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class BitsPool {
    private static transient Logger log = Logger.getLogger(BitsPool.class);
    protected final Map<Integer, Map<Integer, BoxPoint>> baskets;
    protected int xCap;
    protected int yCap;


    public BitsPool(int xCap, int yCap) {
        this.baskets = new HashMap<>(yCap);
        for (int i = 0; i < yCap; i++) {
            baskets.put(i, new HashMap<>());
        }
        this.xCap = xCap;
        this.yCap = yCap;
    }

    public void reset() {
        for (Map<Integer, BoxPoint> pointMap : baskets.values()) {
            pointMap.clear();
        }
    }

    public void put(CompoundObj compoundObj) {
        for (BoxPoint boxPoint : compoundObj.getBoxPoints()) {
            put(boxPoint);
        }
        compoundObj.dispose();

    }

    public void put(BoxPoint boxPoint) {
        baskets.get(boxPoint.getY()).put(boxPoint.getX(), boxPoint);
    }

    Map<Integer, BoxPoint> getYHash(int y) {
        return baskets.get(y);
    }


    /**
     * @param fromY     - current y
     * @param stepsDown - from current to cur+steps
     */
    void moveTopPartDown(int fromY, int stepsDown) {
        Map[] tmpTop = new Map[stepsDown];
        for (int y = fromY + stepsDown - 1, i = 0; y >= 0; y--) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(y);
            if (y >= fromY) {
//                if (!boxPoints.isEmpty()) { // todo
//                    throw new IllegalStateException("!boxPoints.isEmpty()");
//                }
                tmpTop[i++] = boxPoints;
                log.debug("line {} moved to tmp", y);
            } else {
                if (boxPoints != null) {
                    for (BoxPoint boxPoint : boxPoints.values()) {
                        boxPoint.addY(stepsDown);
                    }
                }
                baskets.put(y + stepsDown, boxPoints);
                log.debug("line {} shifted to {}", y, y + stepsDown);
            }
        }

        for (int i = 0; i < tmpTop.length; baskets.put(i, tmpTop[i++])) ;
    }


    private List<Integer> getNextBunch() {
        List<Integer> r = Collections.emptyList();
        Integer prev = null;
        for (int i = yCap - 1; i >= 0; i--) {

            Map<Integer, BoxPoint> boxPoints = baskets.get(i);
            if (boxPoints.isEmpty()) { // todo
                break;
            }
            if (xCap == boxPoints.size()) {
                if (r == Collections.EMPTY_LIST) {
                    r = new ArrayList<Integer>(2);
                }
                if (prev == null) {
                    prev = i;
                }
                if (prev - i > 1) {
                    break;
                } else {
                    r.add(i);
                    prev = i;
                }
            }
        }
        log.info("gotten next bunch: {}", r);
        return r;
    }

    public int eraseLines() {
        List<Integer> r;
        int result = 0;
        while ((r = getNextBunch()) != Collections.EMPTY_LIST) {
            for (int i = 0; i < r.size(); i++, ++result) {
                Integer y = r.get(i);
                Map<Integer, BoxPoint> boxPoints = baskets.get(y);
                RenderEngine.getInstance().removeAll(boxPoints.values()); // animation
                boxPoints.clear();
                if (i == r.size() - 1) {
                    moveTopPartDown(y, r.size());
                }
            }
        }
        return result;
    }

    public boolean checkInPool(CompoundObj compoundObj) {
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        int x = compoundObj.getCursor().getX();
        int y = compoundObj.getCursor().getY();
        for (int i = Math.max(y - yGap,0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    for (BoxPoint point : compoundObj.getBoxPoints()) {
                        if (boxPoint.equals(point)) {
                            log.debug("In pool: {}", point);
                            boxPoint.setInnerColor(Color.WHITE);
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
        for (int i = Math.max(y - yGap, 0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(i);
            for (int j = x - xGap; j <= x; j++) {
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

    private List<BoxPoint> retrieveSection(int xGap, int yGap, int x, int y){
        List<BoxPoint> r  = Collections.emptyList();
        for (int i = Math.max(y - yGap,0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    if(r == Collections.EMPTY_LIST){
                        r = new ArrayList<>();
                    }
                    log.debug("found: {}", boxPoint);
                    boxPoint.setInnerColor(Color.WHITE);
                    r.add(boxPoint);
                }
            }
        }
        return r;
    }

    public boolean checkGapsClash(int xGap, int yGap, int x, int y) {
        log.info("checking gap y [{},{}], x [{},{}]", y - yGap, y, x-xGap, x);
        for (int i = Math.max(y - yGap,0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    log.debug("clashed into: {}", boxPoint);
                    boxPoint.setInnerColor(Color.WHITE);
                    return true;
                }
            }
        }
        return false;
    }


    public boolean checkInPool(int x, int y) {
        Map<Integer, BoxPoint> pointMap = baskets.get(y);
        return pointMap != null && pointMap.get(x) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BitsPool{");
        sb.append("\n");
        sb.append("\n--->");
        for (int i = 0; i < xCap; sb.append("(").append(i++).append(")")) ;
        sb.append("\n");
        for (int j = 0; j < yCap; j++) {
            Map<Integer, BoxPoint> boxPoints = baskets.get(j);
            sb.append("(").append(j).append(") ");

            for (int i = 0; i < xCap; i++) {
                BoxPoint boxPoint = boxPoints.get(i);
                sb.append("[").append(boxPoint == null ? "-" : "X").append("]");
            }
            sb.append("\n");
        }
        return sb.append("}").toString();
    }

}
