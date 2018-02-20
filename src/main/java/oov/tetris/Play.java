package oov.tetris;

import oov.tetris.draw.controller.GameController;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.view.*;
import oov.tetris.proc.BitesPool;
import oov.tetris.proc.FiguresStack;
import oov.tetris.proc.PlayEngine;
import oov.tetris.proc.RenderEngine;
import oov.tetris.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class Play {
    private static Logger log = LoggerFactory.getLogger(Play.class);

    private static final int CW = Integer.valueOf(AppProperties.get("canvas.width"));
    private static final int CH = Integer.valueOf(AppProperties.get("canvas.height"));
    private static final int W = Integer.valueOf(AppProperties.get("field.width"));
    private static final int H = Integer.valueOf(AppProperties.get("field.height"));
    private static final int CAP_X = Integer.valueOf(AppProperties.get("field.capacityX"));
    private static final int CAP_Y = Integer.valueOf(AppProperties.get("field.capacityY"));

    public static void main(String[] args) {
        log.info("starting...");
        ImageComponent imageComponent = initImageComponent();
        RenderEngine renderEngine = RenderEngine.getInstance();
        renderEngine.addListener(imageComponent);
        FpsText fpsText = new FpsText();
        renderEngine.add(fpsText);
        renderEngine.setFpsListener(fpsText);

        GameLayout gameLayout = new GameLayout(CW, CH);
        TextMenu textMenu = new TextMenu(150, 100, Color.DARK_GRAY);
        PlayDesk playDesk = new PlayDesk(CAP_X, CAP_Y, W, H, Color.DARK_GRAY);
        int pdCellW = playDesk.getCellW();
        int pdCellH = playDesk.getCellH();
        PreviewDesk previewDesk = new PreviewDesk(pdCellW, pdCellH, Color.DARK_GRAY,4, 4);
        gameLayout.setPlayDesk(playDesk);
        gameLayout.setTextMenu(textMenu);
        gameLayout.setPreviewDesk(previewDesk);
        renderEngine.add(gameLayout);

        BitesPool bitesPool = new BitesPool(CAP_X, CAP_Y);
        bitesPool.setErasingListener(textMenu);
        final GameController gameController = initController(pdCellW, pdCellH, bitesPool, playDesk, previewDesk);
        PlayEngine playEngine = PlayEngine.getInstance();
        playEngine.addPlayStepListener(gameController::down);
        initKeys(playEngine, gameController);

        AncorControl gameOver = new AnchorText("Game over", Color.WHITE);
        gameOver.setAncor(new Point(W >> 1, H >> 1));

        bitesPool.setOverflowListener(() -> {
            gameController.overTheGame();
            renderEngine.add(gameOver);
        });
        playEngine.start();
    }

    private static void initKeys(PlayEngine playEngine, GameController gameController) {
        playEngine.addKeyCommand(KeyEvent.VK_W, (KeyEvent e) -> gameController.up());// todo for test purposes only
        playEngine.addKeyCommand(KeyEvent.VK_D, (KeyEvent e) -> gameController.right());
        playEngine.addKeyCommand(KeyEvent.VK_A, (KeyEvent e) -> gameController.left());
        playEngine.addKeyCommand(KeyEvent.VK_S, (KeyEvent e) -> gameController.down());

        playEngine.addKeyCommand(KeyEvent.VK_RIGHT, (KeyEvent e) -> gameController.rotateCW());
        playEngine.addKeyCommand(KeyEvent.VK_SPACE, (KeyEvent e) -> gameController.rotateCW());
        playEngine.addKeyCommand(KeyEvent.VK_E, (KeyEvent e) -> gameController.rotateCW());

        playEngine.addKeyCommand(KeyEvent.VK_LEFT, (KeyEvent e) -> gameController.rotateCCW());
        playEngine.addKeyCommand(KeyEvent.VK_Q, (KeyEvent e) -> gameController.rotateCCW());

        playEngine.addKeyCommand(KeyEvent.VK_P, e -> gameController.togglePause());
    }

    private static GameController initController(int cellW, int cellH, BitesPool bitesPool,
                                                 FiguresStack.ChunksStackManagerCurrentListener currentListener,
                                                 FiguresStack.ChunksStackManagerOngoingListener ongoingListener) {

        CompObjFactory compObjFactory = new CompObjFactory(cellW, cellH);
        FiguresStack figuresStack = new FiguresStack(2, compObjFactory);
        figuresStack.setCurrentListener(currentListener);
        figuresStack.setOngoingListener(ongoingListener);
        return new GameController(figuresStack, bitesPool, CAP_X, CAP_Y);
    }


    private static ImageComponent initImageComponent() {
        ImageComponent component = new ImageComponent(CW, CH);
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(component, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        return component;
    }
}
