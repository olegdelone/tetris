package oov.tetris.draw.controller;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.controller.command.*;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.proc.*;
import oov.tetris.util.ScoresUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController {
    private final static Logger log = LoggerFactory.getLogger(GameController.class);

    private final int capX;
    private final int capY;
    private final FiguresStack figuresStack;
    private final BitesPool bitesPool;
    private final PlayEngine playEngine;
    private volatile boolean paused;
    private CompoundObj currentObj;
    private final Player player;
    private final Rule rule;


    public GameController(Player player, FiguresStack figuresStack, BitesPool bitesPool, int capX, int capY) {
        this.player = player;
        playEngine = PlayEngine.getInstance();
        this.figuresStack = figuresStack;
        this.bitesPool = bitesPool;
        this.capX = capX;
        this.capY = capY;
        this.rule = new Rule();
        roll();
    }

    public void right() {
        CtrlCommand command = new MoveRightCommand(bitesPool, currentObj, capX);
        exec(command);
    }

    public void left() {
        CtrlCommand command = new MoveLeftCommand(bitesPool, currentObj);
        exec(command);
    }

    public void up() {
        BoxPoint cursor = currentObj.getCursor();
        exec(() -> {
            if (cursor.getY() > 0) {
                currentObj.moveUp();
            }
        });
    }

    public void down() {
        CtrlCommand command = new MoveDownCommand(bitesPool, currentObj, capY, compoundObj -> {
            bitesPool.put(compoundObj);
            int cnt = bitesPool.eraseLines();
            if(cnt > 0){
                player.addScores(ScoresUtils.calcAndGetScores(cnt));
            }
            if(!bitesPool.checkIfFull()){
                roll();
            }
        });
        exec(command);
    }

    public void rotateCW() {
        CtrlCommand command = new RotateCWCommand(bitesPool, currentObj, capX);
        exec(command);
    }

    public void rotateCCW() {
        CtrlCommand command = new RotateCCWCommand(bitesPool, currentObj, capX);
        exec(command);
    }

    public void togglePause(){
        log.info("togglePause requested");
        togglePauseVar();
        playEngine.setPaused(paused);
    }

    public void overTheGame() {
        if(!paused){
            togglePause();
        }
    }

    @Deprecated
    public void cheatScores() {
        player.addScores(300);
        initLevel();
    }

    private void roll() {
        initLevel();
        currentObj = figuresStack.next();
        log.debug("obj: {}", currentObj);
    }

    private void initLevel() {
        int scores = player.getScores();
        int level = rule.levelUponScores(scores);
        player.setLevel(level);
        playEngine.setGameStepMs(rule.msUponLevel(level));
    }

    private void exec(CtrlCommand ctrlCommand){
        if(paused){
            log.warn("the game is on pause.");
            return;
        }
        ctrlCommand.execute();
    }

    private synchronized void togglePauseVar(){
        paused = !paused;
    }


}
