package oov.tetris.draw;

import oov.tetris.draw.view.AnchorControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class BoxPoint extends AnchorControl implements Cloneable {
    private static Logger log = LoggerFactory.getLogger(BoxPoint.class);

    private Color borderColor;
    private Color innerColor;
    private final int cellW;
    private final int cellH;

    private Point point;

    public BoxPoint(int x, int y, Color borderColor, Color innerColor, int cellW, int cellH) {
        this.point = new Point(x, y);
        this.cellW = cellW;
        this.cellH = cellH;
        this.borderColor = borderColor;
        this.innerColor = innerColor;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }

    public void addX(int x) {
        point.x += x;
    }

    public void addY(int y) {
        point.y += y;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    @Override
    public void draw(Graphics g) {
        int sx = getAnchor().x;
        int sy = getAnchor().y;
        if (point.y < 0) {
            return;
        }
        g.setColor(borderColor);
        int x = point.x * cellW + sx + 1;
        int y = point.y * cellH + sy + 1;
        g.drawRect(x, y, cellW - 2, cellH - 2);
        g.setColor(innerColor);
        g.fillRect(x + 1, y + 1, cellW - 3, cellH - 3);
    }

    // todo prototype
    public static BoxPoint makeBoxPoint(int x, int y, Color color, int w, int h) {
        return new BoxPoint(x, y, Color.GRAY, color, w, h);
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
        BoxPoint r = (BoxPoint) super.clone();
        r.point = (Point) point.clone();
        r.borderColor = new Color(borderColor.getRGB()); // to avoid memory leak
        r.innerColor = new Color(innerColor.getRGB());
        return r;
    }

    public Color getInnerColor() {
        return innerColor;
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BoxPoint) {
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
