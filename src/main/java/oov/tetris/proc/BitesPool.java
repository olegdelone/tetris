package oov.tetris.proc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompoundObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class BitesPool {
    private static final Logger log = LoggerFactory.getLogger(BitesPool.class);

    final Map<Integer, Map<Integer, BoxPoint>> rowMap;
    final int xCap;
    final int yCap;
    private BitesPoolOverflowListener overflowListener;

    public interface BitesPoolOverflowListener {
        void onOverflown();
    }

    public BitesPool(int xCap, int yCap) {
        this.rowMap = Maps.newLinkedHashMapWithExpectedSize(yCap);
        for (int i = 0; i < yCap; i++) {
            rowMap.put(i, Maps.newHashMap());
        }
        this.xCap = xCap;
        this.yCap = yCap;
    }

    public void reset() {
        for (Map<Integer, BoxPoint> pointMap : rowMap.values()) {
            pointMap.clear();
        }
    }

    public boolean checkIfFull(){
        return !getRow(0).values().isEmpty();
    }

    public void put(CompoundObj compoundObj) {
        putBoxes(compoundObj);
        compoundObj.deactivate();
        if(checkIfFull()) {
            if(overflowListener != null){
                overflowListener.onOverflown();
            }
        }
    }


    public void put(BoxPoint boxPoint) {
        int x = boxPoint.getX();
        int y = boxPoint.getY();
        rowMap.get(y).put(x, boxPoint);
    }

    public Map<Integer, BoxPoint> getRow(int y) {
        return rowMap.get(y);
    }

    /**
     * @param fromYIndex     - current y
     * @param stepsUp - from current to cur+steps
     */
    public void moveTopPartDown(int fromYIndex, int stepsUp) {
        List<Map<Integer, BoxPoint>> emptyMiddleRows = Lists.newArrayListWithExpectedSize(stepsUp);
        for (int i = 0; i < stepsUp; i++) {
            Map<Integer, BoxPoint> row = rowMap.remove(fromYIndex - i);
            emptyMiddleRows.add(row);
            log.debug("line {} moved to tmp", fromYIndex - i);
        }
        int y = fromYIndex - stepsUp;
        Map<Integer, BoxPoint> row;
        while (y >= 0 && !(row = rowMap.get(y)).isEmpty()) {
            for (BoxPoint boxPoint : row.values()) {
                boxPoint.addY(stepsUp);
            }
            rowMap.put(y + stepsUp, row);
            log.debug("line {} shifted to {}", y, y + stepsUp);
            y--;
        }
        y++; // step back
        for (int i = 0; i < stepsUp; i++){
            rowMap.put(i+y, emptyMiddleRows.get(i));
            log.debug("line returned back to {}",i+y);
        }
    }

    public Map<Integer, Map<Integer, BoxPoint>> getAllForErase() { // for animation ??
        Map<Integer, Map<Integer, BoxPoint>> result = Maps.newLinkedHashMap();
        for (int y = yCap - 1; y >= 0; y--) { // down to up
            Map<Integer, BoxPoint> row = rowMap.get(y);
            if (xCap == row.size()) {
                result.put(y, row);
            }
        }
        return result;
    }

    protected RowBunch getNextBunch() {
        RowBunch rowBunch = null;
        int startY = -1;
        int cnt = 0;
        int prev = -1;
        for (int y = yCap - 1; y >= 0; y--) { // down to up
            Map<Integer, BoxPoint> boxPoints = rowMap.get(y);
            if (boxPoints.isEmpty()) { // todo
                break;
            }
            if (xCap == boxPoints.size()) {
                if (prev == -1) {
                    startY = prev = y;
                }
                if (prev - y > 1) { // chain broken
                    break;
                }
                cnt++;
                prev = y;
            }
        }
        if(startY != -1){
            rowBunch = new RowBunch(startY, cnt);
        }
        log.info("gotten next bunch: {}", rowBunch);
        return rowBunch;
    }

    public int eraseLines() {
        RowBunch rowBunch;
        int result = 0;
        while ((rowBunch = getNextBunch()) != null) {
            int y = rowBunch.getStartY();
            for (int i = 0; i < rowBunch.getCnt(); i++, ++result) {
                disposeLine(y - i);
            }
            moveTopPartDown(rowBunch.getStartY(), rowBunch.getCnt());
        }
        return result;
    }

    public void setOverflowListener(BitesPoolOverflowListener overflowListener) {
        this.overflowListener = overflowListener;
    }

    public boolean checkInPool(CompoundObj compoundObj) {
        int xGap = compoundObj.getxGap();
        int yGap = compoundObj.getyGap();
        int x = compoundObj.getCursor().getX();
        int y = compoundObj.getCursor().getY();
        for (int i = Math.max(y - yGap,0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = rowMap.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    for (BoxPoint point : compoundObj.getBoxPoints()) {
                        if (boxPoint.equals(point)) {
//                            log.debug("In pool: {}", point);
//                            boxPoint.setInnerColor(Color.yellow);
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
            Map<Integer, BoxPoint> row = rowMap.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint rowPoint = row.get(j);
                if (rowPoint != null) {
                    for (BoxPoint point : compoundObj.getBoxPoints()) {
                        if (rowPoint.getX() == point.getX() && rowPoint.getY() == point.getY() + 1) {
//                            point.setInnerColor(Color.yellow);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void disposeLine(int y){
        Map<Integer, BoxPoint> boxPoints = rowMap.get(y);
        RenderEngine.getInstance().removeAll(boxPoints.values()); // todo fire event
        boxPoints.clear();
    }

    private void putBoxes(CompoundObj compoundObj){
        for (BoxPoint boxPoint : compoundObj.getBoxPoints()) {
            if(boxPoint.getY() >= 0){
                put(boxPoint);
            }
        }
    }

//    private List<BoxPoint> retrieveSection(int xGap, int yGap, int x, int y){
//        List<BoxPoint> r  = Collections.emptyList();
//        for (int i = Math.max(y - yGap,0); i <= y; i++) {
//            Map<Integer, BoxPoint> boxPoints = rowMap.get(i);
//            for (int j = x - xGap; j <= x; j++) {
//                BoxPoint boxPoint = boxPoints.get(j);
//                if (boxPoint != null) {
//                    if(r == Collections.EMPTY_LIST){
//                        r = new ArrayList<>();
//                    }
//                    log.debug("found: {}", boxPoint);
//                    boxPoint.setInnerColor(Color.WHITE);
//                    r.add(boxPoint);
//                }
//            }
//        }
//        return r;
//    }

    public boolean checkGapsClash(int xGap, int yGap, int x, int y) {
        log.info("checking gap y [{},{}], x [{},{}]", y - yGap, y, x-xGap, x);
        for (int i = Math.max(y - yGap,0); i <= y; i++) {
            Map<Integer, BoxPoint> boxPoints = rowMap.get(i);
            for (int j = x - xGap; j <= x; j++) {
                BoxPoint boxPoint = boxPoints.get(j);
                if (boxPoint != null) {
                    log.debug("clashed into: {}", boxPoint);
//                    boxPoint.setInnerColor(Color.yellow);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInPool(int x, int y) {
        return rowMap.get(y).get(x) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BitesPool{");
        sb.append("\n");
        sb.append("\n--->");
        for (int i = 0; i < xCap; sb.append("(").append(i++).append(")")) ;
        sb.append("\n");
        for (int j = 0; j < yCap; j++) {
            Map<Integer, BoxPoint> boxPoints = rowMap.get(j);
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
