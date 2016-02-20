package oov.tetris.proc;

import oov.tetris.Play;
import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.ObjPutListener;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.menu.Cells;
import oov.tetris.draw.menu.CellsPlayground;
import oov.tetris.draw.menu.GameLayout;
import oov.tetris.draw.menu.TextMenu;
import oov.tetris.proc.command.*;
import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;
import oov.tetris.util.ScoresUtils;

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
    private final ChunksStackManager chunksStackManager;

    private BitsPool bitsPool = new BitsPool(C_X, C_Y);


    public void right() {
        CtrlCommand command = new MoveRightCommand(bitsPool, currentObj, C_X);
        command.execute();
    }

    public void left() {
        CtrlCommand command = new MoveLeftCommand(bitsPool, currentObj);
        command.execute();
    }

    public void up() {
        BoxPoint cursor = currentObj.getCursor();
        if (cursor.getY() > 0) {
            currentObj.moveUp();
        }
    }

    public void down() {
        CtrlCommand command = new MoveDownCommand(bitsPool, currentObj, C_Y, compoundObj -> {
            log.debug("onEventCalled");
//            log.info("co: {}", compoundObj);
//            if(compoundObj.getCursor().getY() == 0){
//                Play.overTheGame();
//                return;
//                // todo game over
//            }
            int linesCnt = bitsPool.eraseLines();
            if (linesCnt > 0) {
                lMenu.addScores(ScoresUtils.calcAndGetScores(linesCnt));
            }
            currentObj = chunksStackManager.pop();
            log.debug("obj: {}", currentObj);

            log.debug("obj: {}", currentObj);

            // todo cellw cellH
            cells.addNextCurrentObject(currentObj);
        });
        command.execute();
    }

    public void rotateCW() {
        CtrlCommand command = new RotateCWCommand(bitsPool, currentObj, C_X);
        command.execute();

    }

    public void rotateCCW() {
        CtrlCommand command = new RotateCCWCommand(bitsPool, currentObj, C_X);
        command.execute();
    }

    public GameController() {
        GameLayout gameLayout = new GameLayout(CW, CH);

        Cells rMenu = new Cells(4, 4, 100, 100, Color.DARK_GRAY);
        chunksStackManager = new ChunksStackManager(rMenu, 2);

        lMenu = new TextMenu(150, 100, Color.DARK_GRAY);
        cells = new CellsPlayground(C_X, C_Y, W, H, Color.DARK_GRAY);

        gameLayout.setCells(cells);
        gameLayout.setlMenu(lMenu);
        gameLayout.setrMenu(rMenu);
        RenderEngine.getInstance().add(gameLayout);

        currentObj = chunksStackManager.pop();
//        currentObj.moveTo(C_X >> 1, 0);
        // todo cellw cellH
        cells.addNextCurrentObject(currentObj);

    }





}
