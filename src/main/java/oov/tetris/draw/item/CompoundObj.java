package oov.tetris.draw.item;

import oov.tetris.draw.BoxPoint;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * Created by Olegdelone on 22.07.2015.
 */

public abstract class CompoundObj implements Moveable, Rotateable, Cloneable {
    private static transient Logger log = Logger.getLogger(CompoundObj.class);

    protected BoxPoint cursor;

    protected BoxPoint[] boxPoints;

    private int xGap = -1;
    private int yGap = -1;

    protected CompoundObj(int x, int y, Color color, int cellW, int cellH) {
        boxPoints = obtainFigure(x, y, color, cellW, cellH);
        this.cursor = BoxPoint.makeBoxPoint(x, y, Color.PINK, cellW, cellH);
        initGaps();
    }


    protected abstract BoxPoint[] obtainFigure(int x, int y, Color color, int cellW, int cellH);


//    private static Random random = new Random();
//
//    public void randRotation() { // not good solution
//        for (int i = 0, l = random.nextInt(4); i < l; i++) {
//            rotateCW();
//        }
//    }


    protected boolean isFlat() {
        return getxGap() > getyGap();
    }

    protected void swapGaps() {
        int tmp = getxGap();
        xGap = getyGap();
        yGap = tmp;
//        log.debug("Swapped => x: {}; y: {}", xGap, yGap);

    }

    protected void initGaps() {
        int x = getxGap();
        int y = getyGap();
        log.debug("init gaps: x {}; y {};", x, y);
    }

    public int getxGap() {
        if (xGap == -1) {
            BoxPoint[] arr = boxPoints;
            int minX = arr[0].getX();
            for (int i = 1, l = arr.length; i < l; i++) {
                BoxPoint boxPoint = arr[i];
                int x = boxPoint.getX();
                if (minX > x) {
                    minX = x;
                }
            }
            xGap = cursor.getX() - minX;
        }
        return xGap;
    }

    public int getyGap() {
        if (yGap == -1) {
            BoxPoint[] arr = boxPoints;
            int minY = arr[0].getY();
            for (int i = 1, l = arr.length; i < l; i++) {
                BoxPoint boxPoint = arr[i];
                if (boxPoint.getY() < minY) {
                    minY = boxPoint.getY();
                }
            }
            yGap = cursor.getY() - minY;
        }
        return yGap;
    }


    @Override
    public void rotateCCW() {
        int maxDy = 0;
        int rx = cursor.getX();
        int ry = cursor.getY();
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = -boxPoint.getX() + rx;
            int deltaY = -boxPoint.getY() + ry;

            boxPoint.setX(rx - deltaY);
            boxPoint.setY(ry + deltaX);

            deltaY = boxPoint.getY() - ry;

            if (maxDy < deltaY) {
                maxDy = deltaY;
            }
        }
        if ((maxDy) > 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addY(-maxDy);
            }
        }
        swapGaps();
    }


    @Override
    public void rotateCW() {
        int maxDx = 0;
        int rx = cursor.getX();
        int ry = cursor.getY();
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = -boxPoint.getX() + rx;
            int deltaY = -boxPoint.getY() + ry;

            boxPoint.setX(rx + deltaY);
            boxPoint.setY(ry - deltaX);

            deltaX = boxPoint.getX() - rx;

            if (maxDx < deltaX) {
                maxDx = deltaX;
            }
        }

        if ((maxDx) > 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addX(-maxDx);
            }
        }
        swapGaps();
    }

    @Override
    public void moveDown() {
        moveDown(1);
    }


    @Override
    public void moveUp() {
        moveUp(1);
    }

    @Override
    public void moveLeft() {
        moveLeft(1);
    }


    @Override
    public void moveRight() {
        moveRight(1);
    }

    public void moveDown(int steps) {
        cursor.addY(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(steps);
        }
    }

    public void moveLeft(int steps) {
        cursor.addX(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(-steps);
        }
    }

    public void moveRight(int steps) {
        cursor.addX(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(steps);
        }
    }

    public void moveUp(int steps) {
        cursor.addY(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(-steps);
        }
    }

    public void setColor(Color color) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.setInnerColor(color);
        }
    }


    public void moveTo(int x, int y) {
        int dX = x - cursor.getX();
        int dY = y - cursor.getY();
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(dX);
            boxPoint.addY(dY);
        }
        cursor.setX(x);
        cursor.setY(y);
    }

    public void dispose() {
        cursor = null;
    }

    @Override
    public CompoundObj clone() throws CloneNotSupportedException {
        CompoundObj r = (CompoundObj) super.clone();
        BoxPoint[] boxPointsCloned = new BoxPoint[boxPoints.length];
        for (int i = 0; i < boxPoints.length; i++) {
            boxPointsCloned[i] = boxPoints[i].clone();
        }
        r.boxPoints = boxPointsCloned;
        r.cursor = cursor.clone();
        return r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CompObj_").append("{");
        sb.append("\n--->");
        for (int i = cursor.getX() - xGap; i <= cursor.getX(); sb.append("(").append(i++).append(")")) ;
        sb.append("\n");
        for (int j = cursor.getY() - yGap; j <= cursor.getY(); j++) {

            sb.append("(").append(j).append(") ");
            for (int i = cursor.getX() - xGap; i <= cursor.getX(); i++) {
                BoxPoint found = null;
                for (BoxPoint boxPoint : boxPoints) {
                    if (boxPoint.getY() == j && boxPoint.getX() == i) {
                        found = boxPoint;
                        break;
                    }
                }
                sb.append("[").append(found == null ? "-" : "X").append("]");

            }
            sb.append("\n");
        }
        return sb.append("}").toString();
    }


    public int getOrigCPX() {
        return cursor.getX();
    }


    public BoxPoint getCursor() {
        return cursor;
    }


    public BoxPoint[] getBoxPoints() {
        return boxPoints;
    }
}
