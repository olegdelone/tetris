package oov.tetris.draw.menu;

import oov.tetris.draw.Drawable;

import java.awt.*;


public class TextMenu extends Drawable {

    private int scores;
    private int level;
    private boolean paused;
    private int w;
    private int h;
    private Color color;

    public TextMenu(int w, int h, Color color) {
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    @Override
    public void draw(Graphics g, int x, int y) {

        g.setColor(color);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        int yMid = h>>2;
        int xMid = h>>3;
        g.drawString("Scores: " + scores, x+xMid, y+yMid);
        g.drawString("Level: " + level, x+xMid, y+2*yMid);

        g.drawLine(x,y,x+w,y);
        g.drawLine(x,y+h,x+w,y+h);
        g.drawLine(x,y,x,y+h);
        g.drawLine(x+w,y,x+w,y+h);

        if(paused) {
            g.setColor(Color.RED);
            g.drawString("<Paused>", x+xMid, y+3*yMid);
        }
    }

    public void addScores(int scores) {
        this.scores += scores;
    }
    public void nextLevel() {
        this.level++;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
