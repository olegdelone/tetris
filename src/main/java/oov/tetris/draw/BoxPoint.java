package oov.tetris.draw;

import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Olezha
 * Date: 10.07.14
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class BoxPoint extends Drawable implements Cloneable {
    private Color bodyColor;
    private Color innerColor;
    private final int cellW;
    private final int cellH;
    private Point point;

    public BoxPoint(int x, int y, Color bodyColor, Color innerColor, int cellW, int cellH) {
        this.point = new Point(x, y);
        this.cellW = cellW;
        this.cellH = cellH;
        this.bodyColor = bodyColor;
        this.innerColor = innerColor;
    }

    public Point getPoint() {
        return point;
    }

    public int getX(){
        return point.x;
    }
    public int getY(){
        return point.y;
    }
    public void setX(int x){
//        log.info("set x: {}", x);
        point.x = x;
    }
    public void setY(int y){
//        log.info("set y: {}", y);
        point.y = y;
    }
    public void addX(int x){
        point.x += x;
    }
    public void addY(int y){
        point.y += y;
    }



    public Color getBodyColor() {
        return bodyColor;
    }

    public Color getInnerColor() {
        return innerColor;
    }

    public void setBodyColor(Color bodyColor) {
        this.bodyColor = bodyColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }
    private static transient Logger log = Logger.getLogger(BoxPoint.class);



    @Override
    public void draw(Graphics g, int sx, int sy) {
        g.setColor(bodyColor);
        int x = point.x*cellW + sx+1;
        int y = point.y*cellH + sy+1;
        g.drawRect(x, y, cellW - 2, cellH - 2);
        g.setColor(innerColor);
        g.fillRect(x+1, y+1, cellW-3, cellH-3);
    }

    public static BoxPoint makeBoxPoint(int x, int y, Color color, int w, int h) {
        return new BoxPoint(x, y, Color.YELLOW, color, w, h);
    }

    public static BoxPoint makeBoxLefter(int x, int y, Color color, int w, int h) {
        return makeBoxPoint(--x, y, color, w, h);
    }

    public static BoxPoint makeBoxRighter(int x, int y, Color color, int w, int h) {
        return makeBoxPoint(++x, y, color, w, h);
    }

    public static BoxPoint makeBoxDowner(int x, int y, Color color, int w, int h) {
        return makeBoxPoint(x, ++y, color, w, h);
    }

    public static BoxPoint makeBoxUpper(int x, int y, Color color, int w, int h) {
        return makeBoxPoint(x, --y, color, w, h);
    }

    @Override
    public BoxPoint clone() throws CloneNotSupportedException {
        BoxPoint r = (BoxPoint)super.clone();
        r.point = (Point)point.clone();
        return r;
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof BoxPoint){
            return point.equals(((BoxPoint) obj).getPoint());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "BoxPoint{" +
                "point=" + point +
                '}';
    }
}
