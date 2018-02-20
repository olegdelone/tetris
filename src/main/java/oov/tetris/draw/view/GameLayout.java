package oov.tetris.draw.view;

import oov.tetris.draw.Drawable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.*;
public class GameLayout implements Drawable {
    private static Logger log = LoggerFactory.getLogger(GameLayout.class);

    private final int w;
    private final int h;

    private final int centerX;
    private PlayDesk playDesk;
    private PlayDesk rMenu;
    private TextMenu lMenu;


    public GameLayout(int w, int h) {
        this.w = w;
        this.h = h;
        this.centerX = w >> 1;
    }

    @Override
    public void draw(Graphics g) {
        playDesk.draw(g);
        rMenu.draw(g);
        lMenu.draw(g);
    }

    public void setPlayDesk(PlayDesk playDesk) {
        this.playDesk = playDesk;

        int ch = playDesk.getH();
        int cw = playDesk.getW();

        int remH = h - ch;
        int remW = w - cw;

        int sx = remW >> 1;
        int sy = remH;

        Point cellsSP = new Point(sx, sy);
        playDesk.setAncor(cellsSP);
    }

    public void setPreviewDesk(PlayDesk rMenu) {
        this.rMenu = rMenu;

        int hPad = 0;
        int wPad = 0;
        if(playDesk != null){
            int ch = playDesk.getH();
            int remH = h - ch;
            hPad = (remH - rMenu.getH()) >>1;
            wPad = ((centerX-lMenu.getW())>>1);
        }
        Point rMenuSP = new Point(centerX + wPad, hPad);
        rMenu.setAncor(rMenuSP);
    }

    public void setTextMenu(TextMenu lMenu) {
        this.lMenu = lMenu;
        int hPad = 0;
        int wPad = 0;
        if(playDesk != null){
            int ch = playDesk.getH();
            int remH = h - ch;
            hPad = (remH - lMenu.getH()) >>1;
            wPad = ((centerX-lMenu.getW())>>1);
        }
        Point lMenuSP = new Point(wPad, hPad);
        lMenu.setAncor(lMenuSP);
    }
}
