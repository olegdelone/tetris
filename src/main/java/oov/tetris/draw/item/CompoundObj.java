package oov.tetris.draw.item;

import oov.tetris.draw.*;
import oov.tetris.draw.menu.Cells;
import oov.tetris.util.Logger;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Olegdelone on 22.07.2015.
 */

public abstract class CompoundObj extends Drawable implements Moveable, Rotateable, Cloneable {
    private static transient Logger log = Logger.getLogger(CompoundObj.class);

    private int xRotationShift;
    protected BoxPoint cursor; // bottomLeft
    protected BoxPoint rotationPoint;

    protected BoxPoint[] boxPoints;

    private int xGap = -1;
    private int yGap = -1;

    protected CompoundObj(int x, int y, Color color, int cellW, int cellH, int xRotationShift) {
        boxPoints = obtainFigure(x, y, color, cellW, cellH);
        this.xRotationShift = xRotationShift;
        this.rotationPoint = BoxPoint.makeBoxPoint(x + xRotationShift, y, Color.CYAN, cellW, cellH);
        this.cursor = BoxPoint.makeBoxPoint(x, y, Color.PINK, cellW, cellH);
        initGaps();
    }



    public CompoundObj(int x, int y, Color color, int cellW, int cellH) {
        this(x, y, color, cellW, cellH, 0);
    }

    protected abstract BoxPoint[] obtainFigure(int x, int y, Color color, int cellW, int cellH);


//    private static Random random = new Random();
//
//    public void randRotation() { // not good solution
//        for (int i = 0, l = random.nextInt(4); i < l; i++) {
//            rotateCW();
//        }
//    }


    public BoxPoint getCursor() {
        return cursor;
    }

    private void swapGaps() {

        int tmp = getxGap();
        xGap = getyGap();
        yGap = tmp;
        log.debug("Swapped => x: {}; y: {}",xGap, yGap);

    }

    protected void initGaps() {
        int x = getxGap();
        int y = getyGap();
        log.debug("init gaps: x {}; y {};", x ,y);
    }

    public int getxGap() {
        if (xGap == -1) {
            BoxPoint[] arr = boxPoints;
            int maxX = arr[0].getX();
            for (int i = 1, l = arr.length; i < l; i++) {
                BoxPoint boxPoint = arr[i];
                if (boxPoint.getX() > maxX) {
                    maxX = boxPoint.getX();
                }
            }
            xGap = maxX -  cursor.getX();
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
    public void draw(Graphics g, int x, int y) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.draw(g, x, y);
        }
        cursor.draw(g, x, y);
//        rotationPoint.draw(g, x, y);
    }

    @Override
    public void rotateCW() {
        int maxDy = 0;

        int rx = rotationPoint.getX();
        int ry = rotationPoint.getY();
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = boxPoint.getX() - rx;
            int deltaY = boxPoint.getY() - ry;

            boxPoint.setX(rx - deltaY);
            boxPoint.setY(ry + deltaX);

            deltaY = boxPoint.getY() - ry;// - xRotationShift;

            if (maxDy < deltaY) {
                maxDy = deltaY;
            }
        }
        if ((maxDy) > 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addY(-maxDy);
            }
            cursor.addX(xRotationShift);
        } else {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addX(-xRotationShift);
            }
            cursor.addX(-xRotationShift);
        }
        swapGaps();
    }


    @Override
    public void rotateCCW() {
        int minDx = 0;
        int rx = rotationPoint.getX();
        int ry = rotationPoint.getY();
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = boxPoint.getX() - rx;
            int deltaY = boxPoint.getY() - ry;

            boxPoint.setX(rx + deltaY);
            boxPoint.setY(ry - deltaX);

            deltaX = boxPoint.getX() - rx;

            if (minDx > deltaX) {
                minDx = deltaX;
            }
        }

        if ((minDx) < 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addX(-minDx - xRotationShift);
            }
            cursor.addX(-xRotationShift);
        } else {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addY(-xRotationShift);
            }
            cursor.addX(xRotationShift);
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
//        Cells cells = (Cells) tetrisControl;
        if (cursor.getX() > 0) {
            moveLeft(1);
        }
    }


    @Override
    public void moveRight() {
        Cells cells = (Cells) tetrisControl;
        if (cursor.getX() + getxGap() < cells.getxCapacity() - 1) {
            moveRight(1);
        }
    }

    private void moveDown(int steps) {
        cursor.addY(steps);
        rotationPoint.addY(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(steps);
        }
    }

    private void moveLeft(int steps) {
        cursor.addX(-steps);
        rotationPoint.addX(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(-steps);
        }
    }

    private void moveRight(int steps) {
        cursor.addX(steps);
        rotationPoint.addX(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(steps);
        }
    }

    private void moveUp(int steps) {
        cursor.addY(-steps);
        rotationPoint.addY(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(-steps);
        }
    }

    public void setColor(Color color) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.setInnerColor(color);
        }
    }

    public BoxPoint[] getBoxPoints() {
        return boxPoints;
    }


    public void dispose() {
//        RenderEngine engine = RenderEngine.getInstance();
//        engine.remove(rotationPoint);
//        engine.remove(cursor);
        rotationPoint = null;
        cursor = null;
    }

    @Override
    public CompoundObj clone() throws CloneNotSupportedException {
        CompoundObj r =  (CompoundObj)super.clone();
        BoxPoint[] boxPointsCloned = new BoxPoint[boxPoints.length];
        for (int i = 0; i < boxPoints.length; i++) {
            boxPointsCloned[i] = boxPoints[i].clone();
        }
        r.boxPoints = boxPointsCloned;
        r.cursor = cursor.clone();
        r.rotationPoint = rotationPoint.clone();
        return r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CompObj_").append("{");
        sb.append("\n--->");
        for (int i = cursor.getX(); i <= cursor.getX() + xGap; sb.append("(").append(i++).append(")"));
        sb.append("\n");
        for (int j = cursor.getY()-yGap; j <= cursor.getY(); j++) {

            sb.append("(").append(j).append(") ");
            for (int i = cursor.getX(); i <= cursor.getX() + xGap; i++) {
                BoxPoint found = null;
                for (BoxPoint boxPoint : boxPoints) {
                    if(boxPoint.getY() == j && boxPoint.getX() == i){
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
}
