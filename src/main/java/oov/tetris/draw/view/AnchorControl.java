package oov.tetris.draw.view;


import oov.tetris.draw.Drawable;

import java.awt.*;


public abstract class AnchorControl implements Drawable {
    private final static Point ZERO_POINT = new Point(0, 0);
    private Point anchor = ZERO_POINT;

//    private Point cachedPoint;
//    public void draw(Graphics g) {
//        int sx, sy;
//        Point point;
//        if (cachedPoint == null) {
//            if ((point = getAnchor()) != null) {
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
//    public abstract void draw(Graphics g, int x, int y);

    public void setAnchor(Point anchor){
        this.anchor = anchor;
    }
    public Point getAnchor() {
        return anchor;
    }
}
