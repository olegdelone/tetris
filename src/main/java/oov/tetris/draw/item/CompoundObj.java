package oov.tetris.draw.item;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.view.AncorControl;
import oov.tetris.proc.RenderEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collections;


public abstract class CompoundObj extends AncorControl implements Moveable, Rotateable, Cloneable {
    private static Logger log = LoggerFactory.getLogger(CompoundObj.class);
    private int xGap;
    private int yGap;
    private BoxPoint cursor;
    private BoxPoint[] boxPoints;
    protected Color color;

    protected CompoundObj(int x, int y, Color color, int cellW, int cellH) {
        boxPoints = obtainFigure(x, y, color, cellW, cellH);
        this.cursor = BoxPoint.makeBoxPoint(x, y, Color.PINK, cellW, cellH);
        xGap = initXGap();
        yGap = initYGap();
        this.color = color;
    }

    protected abstract BoxPoint[] obtainFigure(int x, int y, Color color, int cellW, int cellH);

    @Override
    public void setAncor(Point ancor) {
        super.setAncor(ancor);
        cursor.setAncor(ancor);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.setAncor(ancor);
        }
    }

    @Override
    public void draw(Graphics g) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.draw(g);
        }
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

    public void deactivate() {
        cursor = null;
        RenderEngine.getInstance().removeAll(Collections.singleton(this));
        for (BoxPoint boxPoint : boxPoints) {
            RenderEngine.getInstance().add(boxPoint);
        }
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

    public int getxGap() {
        return xGap;
    }

    public int getyGap() {
        return yGap;
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

    boolean isFlat() {
        return getxGap() > getyGap();
    }

    private int initXGap(){
        BoxPoint[] arr = boxPoints;
        int minX = arr[0].getX();
        for (int i = 1, l = arr.length; i < l; i++) {
            BoxPoint boxPoint = arr[i];
            int x = boxPoint.getX();
            if (minX > x) {
                minX = x;
            }
        }
        return cursor.getX() - minX;
    }


    private int initYGap(){
        BoxPoint[] arr = boxPoints;
        int minY = arr[0].getY();
        for (int i = 1, l = arr.length; i < l; i++) {
            BoxPoint boxPoint = arr[i];
            if (boxPoint.getY() < minY) {
                minY = boxPoint.getY();
            }
        }
        return cursor.getY() - minY;
    }


    private void swapGaps() {
        int tmp = getxGap();
        xGap = getyGap();
        yGap = tmp;
    }

}
