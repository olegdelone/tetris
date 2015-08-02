package oov.tetris.proc;

import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.menu.Cells;
import oov.tetris.draw.menu.GameLayout;
import oov.tetris.draw.menu.TextMenu;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.item.Iobj;
import oov.tetris.manager.ChunksFactory;
import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 07.09.13 18:15: Original version (OOBUKHOV)<br/>
 */
public class GameController {
    private static transient Logger log = Logger.getLogger(GameController.class);

    static final int CELL_W = Integer.valueOf(AppProperties.get("canvas.cell.width"));
    static final int CELL_H = Integer.valueOf(AppProperties.get("canvas.cell.height"));
    static final int CW = Integer.valueOf(AppProperties.get("canvas.width"));
    static final int CH = Integer.valueOf(AppProperties.get("canvas.height"));

    static final int W = Integer.valueOf(AppProperties.get("field.width"));
    static final int H = Integer.valueOf(AppProperties.get("field.height"));
    static final int C_X = Integer.valueOf(AppProperties.get("field.capacityX"));
    static final int C_Y = Integer.valueOf(AppProperties.get("field.capacityY"));


    private short tick;

    public short getTick() {
        return tick;
    }

    CompoundObj compoundObj = CompObjFactory.makeRandObj(5,5, 25,25);

    public void setAction(int eventCode) {
        if (eventCode == KeyEvent.VK_D) {
            compoundObj.moveRight();
        } else if (eventCode == KeyEvent.VK_A) {
            compoundObj.moveLeft();
        } else if (eventCode == KeyEvent.VK_W) {
            compoundObj.moveUp(); // todo for test purposes only
        } else if (eventCode == KeyEvent.VK_S) {
            compoundObj.moveDown();
        } else if (eventCode == KeyEvent.VK_RIGHT || eventCode == KeyEvent.VK_SPACE || eventCode == KeyEvent.VK_E) {
            compoundObj.rotateCW();
        } else if (eventCode == KeyEvent.VK_LEFT || eventCode == KeyEvent.VK_Q) {
            compoundObj.rotateCCW();
        }
    }


    public GameController() {
        GameLayout gameLayout = new GameLayout(CW, CH);

        Cells rMenu = new Cells(4, 4, 100, 100, Color.DARK_GRAY);
        TextMenu lMenu = new TextMenu(150, 100, Color.DARK_GRAY);
        Cells cells = new Cells(C_X, C_Y, W, H, Color.DARK_GRAY);
        gameLayout.setCells(cells);
        gameLayout.setlMenu(lMenu);
        gameLayout.setrMenu(rMenu);

        compoundObj.setGameContainer(gameLayout.getCells());


        RenderEngine.getInstance().add(compoundObj);
        RenderEngine.getInstance().add(gameLayout);
    }

    public final static int GAME_TIME = 100; // todo level parametrized

    public void run() {

//        long timeStart = System.currentTimeMillis();
//
//        while (true) {
//            long timeCurrent = System.currentTimeMillis();
//
//            if (timeCurrent - timeStart >= GAME_TIME) {
//                if (++tick % 3 == 0) {
//
//
//
//                }
//                timeStart = timeCurrent;
//            }
//        }
    }

}
