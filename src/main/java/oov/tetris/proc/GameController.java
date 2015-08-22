package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.ObjPutListener;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.menu.Cells;
import oov.tetris.draw.menu.GameLayout;
import oov.tetris.draw.menu.TextMenu;
import oov.tetris.proc.command.CtrlCommand;
import oov.tetris.proc.command.MoveDownCommand;
import oov.tetris.proc.command.RotateCCWCommand;
import oov.tetris.proc.command.RotateCWCommand;
import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * 07.09.13 18:15: Original version (OOBUKHOV)<br/>
 */
public class GameController {
    private static transient Logger log = Logger.getLogger(GameController.class);

    //    static final int CELL_W = Integer.valueOf(AppProperties.get("canvas.cell.width"));
//    static final int CELL_H = Integer.valueOf(AppProperties.get("canvas.cell.height"));
    static final int CW = Integer.valueOf(AppProperties.get("canvas.width"));
    static final int CH = Integer.valueOf(AppProperties.get("canvas.height"));

    static final int W = Integer.valueOf(AppProperties.get("field.width"));
    static final int H = Integer.valueOf(AppProperties.get("field.height"));
    static final int C_X = Integer.valueOf(AppProperties.get("field.capacityX"));
    static final int C_Y = Integer.valueOf(AppProperties.get("field.capacityY"));

    private CompoundObj currentObj;
    Cells cells;
    private BitsPool bitsPool = new BitsPool(C_X, C_Y);
    private short tick;

    public short getTick() {
        return tick;
    }

//    CompoundObj compoundObj = CompObjFactory.makeRandObj(5,5, 25,25);

    public void right() {
        BoxPoint cursor = currentObj.getCursor();
        if (cursor.getX() < C_X - 1) {
            currentObj.moveRight();
        }
    }

    public void left() {
        BoxPoint cursor = currentObj.getCursor();
        if (cursor.getX() - currentObj.getxGap() > 0) {
            currentObj.moveLeft();
        }
    }

    public void up() {
        BoxPoint cursor = currentObj.getCursor();
        if (cursor.getY() > 0) {
            currentObj.moveUp();
        }
    }

    public void down() {
        CtrlCommand command = new MoveDownCommand(bitsPool, currentObj, C_Y, new ObjPutListener() {
            @Override
            public void onEvent(CompoundObj compoundObj) {
                log.debug("onEventCalled");
                currentObj = cells.addNextCurrentObject();
            }
        });
        command.execute();
    }

    public void rotateCW() {
        CtrlCommand command = new RotateCWCommand(bitsPool,currentObj,C_X);
        command.execute();
    }

    public void rotateCCW() {
        CtrlCommand command = new RotateCCWCommand(bitsPool,currentObj,C_X);
        command.execute();
    }

//    private boolean checkIsAllowed(CompoundObj currentObj) {
//        return C_X;
//    }


    public GameController() {
        GameLayout gameLayout = new GameLayout(CW, CH);

        Cells rMenu = new Cells(4, 4, 100, 100, Color.DARK_GRAY);
        TextMenu lMenu = new TextMenu(150, 100, Color.DARK_GRAY);
        cells = new Cells(C_X, C_Y, W, H, Color.DARK_GRAY);

        gameLayout.setCells(cells);
        gameLayout.setlMenu(lMenu);
        gameLayout.setrMenu(rMenu);
        RenderEngine.getInstance().add(gameLayout);

        currentObj = cells.addNextCurrentObject();

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
