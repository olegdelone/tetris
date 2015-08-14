package oov.tetris.draw.menu;

import oov.tetris.draw.Drawable;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * Created by Olegdelone on 25.07.2015.
 */
public class GameLayout extends Drawable implements TetrisControl {

    private static transient Logger log = Logger.getLogger(GameLayout.class);

    private final int w;
    private final int h;

    private final int centerX;
    private  Cells cells;
    private  Cells rMenu;

    private Point cellsSP;
    private Point rMenuSP;
    private Point lMenuSP;
    private  TextMenu lMenu;


    public Cells getCells() {
        return cells;
    }

    public GameLayout(int w, int h) {
        this.w = w;
        this.h = h;
        this.centerX = w >> 1;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        cells.draw(g);
        rMenu.draw(g);
        lMenu.draw(g);
    }

    @Override
    public Point getShiftPoint(Drawable drawable) {
        if(drawable  == cells){
            return cellsSP;
        }
        if (drawable == rMenu){
            return rMenuSP;
        }
        if (drawable == lMenu){
            return lMenuSP;
        }
        return null;
    }

    public void setCells(Cells cells) {
        this.cells = cells;
        cells.setTetrisControl(this);

        int ch = cells.getH();
        int cw = cells.getW();

        int remH = h - ch;
        int remW = w - cw;


        int sx = remW >> 1;
        int sy = remH;

        cellsSP = new Point(sx, sy);
    }

    public void setrMenu(Cells rMenu) {
        this.rMenu = rMenu;
        rMenu.setTetrisControl(this);

        int hh = centerX>>1;

        int hPad = 0;
        int wPad = 0;
        if(cells != null){
            int ch = cells.getH();
            int remH = h - ch;
            hPad = (remH - rMenu.getH()) >>1;
            wPad = ((centerX-lMenu.getW())>>1);
        }
        rMenuSP = new Point(centerX + wPad, hPad);
    }

    public void setlMenu(TextMenu lMenu) {
        this.lMenu = lMenu;
        lMenu.setTetrisControl(this);

        int hh = centerX>>1;

        int hPad = 0;
        int wPad = 0;
        if(cells != null){
            int ch = cells.getH();
            int remH = h - ch;
            hPad = (remH - lMenu.getH()) >>1;
            wPad = ((centerX-lMenu.getW())>>1);
        }
        lMenuSP = new Point(wPad, hPad);

    }
}
