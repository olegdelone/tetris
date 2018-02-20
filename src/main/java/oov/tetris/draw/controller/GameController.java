package oov.tetris.draw.controller;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.controller.command.*;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.view.PlayDesk;
import oov.tetris.draw.view.PreviewDesk;
import oov.tetris.draw.view.GameLayout;
import oov.tetris.draw.view.TextMenu;
import oov.tetris.proc.BitsPool;
import oov.tetris.proc.FiguresStack;
import oov.tetris.proc.RenderEngine;
import oov.tetris.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class GameController {
    private static Logger log = LoggerFactory.getLogger(GameController.class);

    private static final int CW = Integer.valueOf(AppProperties.get("canvas.width"));
    private static final int CH = Integer.valueOf(AppProperties.get("canvas.height"));
    private static final int W = Integer.valueOf(AppProperties.get("field.width"));
    private static final int H = Integer.valueOf(AppProperties.get("field.height"));
    private static final int CAP_X = Integer.valueOf(AppProperties.get("field.capacityX"));
    private static final int CAP_Y = Integer.valueOf(AppProperties.get("field.capacityY"));

    private CompoundObj currentObj;
    private final PlayDesk playDesk;
    private final PreviewDesk previewDesk;
    private final FiguresStack figuresStack;
    private BitsPool bitsPool;

    public GameController() {
        GameLayout gameLayout = new GameLayout(CW, CH);

        TextMenu textMenu = new TextMenu(150, 100, Color.DARK_GRAY);
        bitsPool = new BitsPool(CAP_X, CAP_Y, textMenu);
        playDesk = new PlayDesk(CAP_X, CAP_Y, W, H, Color.DARK_GRAY);
        previewDesk = new PreviewDesk(playDesk.getCellW(), playDesk.getCellH(), Color.DARK_GRAY,4, 4);

        gameLayout.setPlayDesk(playDesk);
        gameLayout.setTextMenu(textMenu);
        gameLayout.setPreviewDesk(previewDesk);
        RenderEngine.getInstance().add(gameLayout);
        CompObjFactory compObjFactory = new CompObjFactory(playDesk.getCellW(), playDesk.getCellH());
        figuresStack = new FiguresStack(2, compObjFactory);

        roll();
    }

    private void roll(){
        currentObj = figuresStack.next();
        CompoundObj ongoing = figuresStack.getOngoing();
        previewDesk.placeObj(ongoing);
        log.debug("obj: {}", currentObj);
        playDesk.placeObj(currentObj);
    }

    public void right() {
        CtrlCommand command = new MoveRightCommand(bitsPool, currentObj, CAP_X);
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
        CtrlCommand command = new MoveDownCommand(bitsPool, currentObj, CAP_Y, compoundObj -> {
            log.debug("onEventCalled");
//            log.info("co: {}", compoundObj);
//            if(compoundObj.getCursor().getY() == 0){
//                Play.overTheGame();
//                return;
//                // todo game over
//            }
            bitsPool.eraseLines();
            roll();
        });
        command.execute();
    }

    public void rotateCW() {
        CtrlCommand command = new RotateCWCommand(bitsPool, currentObj, CAP_X);
        command.execute();

    }

    public void rotateCCW() {
        CtrlCommand command = new RotateCCWCommand(bitsPool, currentObj, CAP_X);
        command.execute();
    }







}
