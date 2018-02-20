package oov.tetris.proc;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;


public class PlayEngine {
    private static transient Logger log = LoggerFactory.getLogger(PlayEngine.class);
    private static volatile PlayEngine instance;
    private final static int GAME_TIME = 300;

    private volatile boolean paused;

    private final RenderEngine renderEngine;
    private final Map<Integer, KeyCommand> keyCommandMap;
    private final List<PlayStepListener> playStepListeners;

    private PlayEngine() {
        playStepListeners = Lists.newArrayList();
        keyCommandMap = Maps.newHashMap();
        renderEngine = RenderEngine.getInstance();
    }

    public static PlayEngine getInstance() {
        if (instance == null) {
            synchronized (RenderEngine.class) {
                if (instance == null) {
                    instance = new PlayEngine();
                }
            }
        }
        return instance;
    }

    public void addKeyCommand(int keyCode, KeyCommand keyCommand){
        keyCommandMap.put(keyCode, keyCommand);
    }

    public void addPlayStepListener(PlayStepListener playStepListener){
        playStepListeners.add(playStepListener);
    }




    public void start() {
        initKeyMaps();
        startLoop();
    }

    private void notifyListeners(){
        for (PlayStepListener playStepListener : playStepListeners) {
            playStepListener.executeStep();
        }
    }

    private void initKeyMaps() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                int eventCode = e.getKeyCode();
                synchronized (this) {
                    KeyCommand keyCommand = keyCommandMap.get(eventCode);
                    if (keyCommand != null) {
                        keyCommand.execute(e);
                    }
                }
            }
            return true;
        });
    }

    public void setPaused(boolean flag) {
        paused = flag;
        if (!paused) {
            synchronized (this) {
                log.info("notifying...");
                this.notify();
            }
        }
    }

    private void startLoop() {
        long timeStart = System.currentTimeMillis();

        while (true) {
            while (paused) {
                synchronized (this) {
                    try {
                        log.info("on waiting...");
                        this.wait();
                        log.info("awaking...");
                    } catch (InterruptedException e) {
                        renderEngine.stop();
                        return;
                    }
                }
            }
            long timeCurrent = System.currentTimeMillis();
            if (timeCurrent - timeStart >= GAME_TIME) {
                notifyListeners();
                timeStart = timeCurrent;
            }
        }
    }


    public interface KeyCommand {
        void execute(KeyEvent e);
    }

    public interface PlayStepListener {
        void executeStep();
    }
}
