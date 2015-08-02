package oov.tetris.draw.item;

import oov.tetris.draw.*;
import oov.tetris.draw.menu.GameContainer;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * Created by Olegdelone on 22.07.2015.
 */

public abstract class CompoundObj extends Drawable implements Moveable, Rotateable, GameContainer {
    private static transient Logger log = Logger.getLogger(CompoundObj.class);

    private int xRotationShift;
    protected BoxPoint position;

    protected BoxPoint[] boxPoints;

    public CompoundObj(int x, int y, Color color, int cellW, int cellH, int xRotationShift) {
        this(x, y, color, cellW, cellH);
        this.xRotationShift = xRotationShift;
        this.position.addX(xRotationShift);
    }

    public CompoundObj(int x, int y, Color color, int cellW, int cellH) {
        this.position = BoxPoint.makeBoxPoint(x, y, Color.PINK, cellW, cellH);
        position.setGameContainer(this);
    }


    @Override
    public Point getShiftPoint(Drawable drawable) {
        return gameContainer.getShiftPoint(this);

    }

//    private static Random random = new Random();
//
//    public void randRotation() { // not good solution
//        for (int i = 0, l = random.nextInt(4); i < l; i++) {
//            rotateCW();
//        }
//    }

    @Override
    public void draw(Graphics g, int x, int y) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.draw(g);
        }
        position.draw(g);
    }

    @Override
    public void rotateCW() {
        int maxDy = 0;
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = boxPoint.getX() - position.getX();
            int deltaY = boxPoint.getY() - position.getY();

            boxPoint.setX(position.getX() - deltaY);
            boxPoint.setY(position.getY() + deltaX);

            deltaY = boxPoint.getY() - position.getY();

            if (maxDy < deltaY) {
                maxDy = deltaY;
            }
        }
        if ((maxDy = maxDy - xRotationShift) > 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addY(-maxDy);
            }
        }
    }

    @Override
    public void rotateCCW() {
        int minDx = 0;
        for (BoxPoint boxPoint : boxPoints) {
            int deltaX = boxPoint.getX() - position.getX();
            int deltaY = boxPoint.getY() - position.getY();

            boxPoint.setX(position.getX() + deltaY);
            boxPoint.setY(position.getY() - deltaX);

            deltaX = boxPoint.getX() - position.getX();

            if (minDx > deltaX) {
                minDx = deltaX;
            }
        }

        if ((minDx = minDx + xRotationShift) < 0) {
            for (BoxPoint boxPoint : boxPoints) {
                boxPoint.addX(-minDx);
            }
        }
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
        position.addY(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(steps);
        }
    }

    public void moveLeft(int steps) {
        position.addX(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(-steps);
        }
    }

    public void moveRight(int steps) {
        position.addX(steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addX(steps);
        }
    }

    public void moveUp(int steps) {
        position.addY(-steps);
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.addY(-steps);
        }
    }

    public void setColor(Color color) {
        for (BoxPoint boxPoint : boxPoints) {
            boxPoint.setInnerColor(color);
        }
    }


}
