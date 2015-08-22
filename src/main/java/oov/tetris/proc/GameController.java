package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.ObjPutListener;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.menu.Cells;
import oov.tetris.draw.menu.GameLayout;
import oov.tetris.draw.menu.TextMenu;
import oov.tetris.proc.command.*;
import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;

import java.awt.*;

/**
 * 07.09.13 18:15: Original version (OOBUKHOV)<br/>
 */
public class GameController {
    private static transient Logger log = Logger.getLogger(GameController.class);

    static final int CW = Integer.valueOf(AppProperties.get("canvas.width"));
    static final int CH = Integer.valueOf(AppProperties.get("canvas.height"));

    static final int W = Integer.valueOf(AppProperties.get("field.width"));
    static final int H = Integer.valueOf(AppProperties.get("field.height"));
    static final int C_X = Integer.valueOf(AppProperties.get("field.capacityX"));
    static final int C_Y = Integer.valueOf(AppProperties.get("field.capacityY"));

    private CompoundObj currentObj;
    private final Cells cells;
    private final TextMenu lMenu;

    private BitsPool bitsPool = new BitsPool(C_X, C_Y);
    private short tick;

    public short getTick() {
        return tick;
    }

    public void right() {
        CtrlCommand command = new MoveRightCommand(bitsPool,currentObj,C_X);
        command.execute();
    }

    public void left() {
        CtrlCommand command = new MoveLeftCommand(bitsPool,currentObj,C_X);
        command.execute();
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
                int linesCnt = bitsPool.eraseLines();
                if(linesCnt > 0){
                    lMenu.addScores(calcScores(linesCnt));
                }
                currentObj = cells.addNextCurrentObject();
            }
        });
        command.execute();
    }

    private static int calcScores(int a){
        if(a<=0){
            throw new IllegalArgumentException("arg a<=0");
        }
        if(a == 1){
            return 100;
        }
        return 2*calcScores(--a) + 100;
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
        lMenu = new TextMenu(150, 100, Color.DARK_GRAY);
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
