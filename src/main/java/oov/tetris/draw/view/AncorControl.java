package oov.tetris.draw.view;


import oov.tetris.draw.Drawable;

import java.awt.*;


public abstract class AncorControl implements Drawable {
    private final static Point ZERO_POINT = new Point(0, 0);
    private Point ancor = ZERO_POINT;

//    private Point cachedPoint;
//
//    public void draw(Graphics g) {
//        int sx, sy;
//        Point point;
//        if (cachedPoint == null) {
//            if ((point = getAncor()) != null) {
//                cachedPoint = point;
//            } else {
//                cachedPoint = ZERO_POINT;
//            }
//        }
//        sx = cachedPoint.x;
//        sy = cachedPoint.y;
//
//        draw(g, sx, sy);
//    }
//
//    public abstract void draw(Graphics g, int x, int y);

    public void setAncor(Point ancor){
        this.ancor = ancor;
    }
    public Point getAncor() {
        return ancor;
    }
}
