package oov.tetris.draw;

import oov.tetris.draw.menu.GameContainer;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Olezha
 * Date: 10.07.14
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class Drawable {
    protected GameContainer gameContainer;
    private final static Point ZERO_POINT = new Point(0, 0);
    private Point cachedPoint;

    public void setGameContainer(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public void draw(Graphics g) {
        int sx, sy;
        Point point;
        if (cachedPoint == null) {
            if (gameContainer != null && (point = gameContainer.getShiftPoint(this)) != null) {
                cachedPoint = point;
            } else {
                cachedPoint = ZERO_POINT;
            }
        }
        sx = cachedPoint.x;
        sy = cachedPoint.y;

        draw(g, sx, sy);
    }

    public abstract void draw(Graphics g, int x, int y);

}
