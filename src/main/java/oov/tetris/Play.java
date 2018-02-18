package oov.tetris;

import oov.tetris.draw.Drawable;
import oov.tetris.proc.GameController;
import oov.tetris.proc.RenderEngine;
import oov.tetris.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class Play {

    private static volatile boolean paused = false;
    private static transient Logger log = LoggerFactory.getLogger(Play.class);
    private final static int GAME_TIME = 100; // todo level parametrized

    private static final int w = Integer.valueOf(AppProperties.get("canvas.width"));
    private static final int h = Integer.valueOf(AppProperties.get("canvas.height"));

    private final static BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    private final static JComponent component = new OverriddenComponent();

    public static void main(String[] args) {
        initComponents();
        initRenderCallback();

        RenderEngine renderEngine = RenderEngine.getInstance();

        final GameController gameController = new GameController();


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                int eventCode = e.getKeyCode();
                synchronized (renderEngine) {
                    if (eventCode == KeyEvent.VK_P) {
                        paused = !paused;
                        if (!paused) {
                            log.debug("notifying...");
                            renderEngine.notify();
                        }
                    }
                    if (paused) {
                        return false;
                    }
                    if (eventCode == KeyEvent.VK_D) {
                        gameController.right();
                    } else if (eventCode == KeyEvent.VK_A) {
                        gameController.left();
                    } else if (eventCode == KeyEvent.VK_W) {
                        gameController.up(); // todo for test purposes only
                    } else if (eventCode == KeyEvent.VK_S) {
                        gameController.down();
                    } else if (eventCode == KeyEvent.VK_RIGHT || eventCode == KeyEvent.VK_SPACE || eventCode == KeyEvent.VK_E) {
                        gameController.rotateCW();
                    } else if (eventCode == KeyEvent.VK_LEFT || eventCode == KeyEvent.VK_Q) {
                        gameController.rotateCCW();
                    }
                }
            }
            return true;
        });

        int tick = 0;
        long timeStart = System.currentTimeMillis();

        while (true) {
            while (paused) {
                synchronized (renderEngine) {
                    try {
                        log.debug("on waiting...");
                        renderEngine.wait();
                        log.debug("awaking...");
                    } catch (InterruptedException e) {
                        shutdown();
                        return;
                    }
                }
            }
            long timeCurrent = System.currentTimeMillis();

            if (timeCurrent - timeStart >= GAME_TIME) {
                if (++tick % 3 == 0) {
                    gameController.down();
                }
                timeStart = timeCurrent;
            }

        }

//        try {
//        } catch (IllegalStateException e) {
//            Thread thread = RenderEngine.getInstance().stop();
//            try {
//                if (thread.isAlive()) {
//                    log.info("joining to the thread...");
//                    thread.join();
//                    log.info("join released...");
//                }
//            } catch (InterruptedException e1) {
//                log.warn("join interrupted.", e1);
//            }
//            RenderEngine.getInstance().fullRender();
//            log.error("e: ", e);
//            Color bgColor = Color.WHITE;
//            Graphics gr = image.getGraphics();
//            gr.setColor(bgColor);
//            gr.drawString("Game over", w >> 1, h >> 1);
//            render();
//        }

    }

    public static void overTheGame() {
        paused = true;
        RenderEngine.getInstance().add(new Drawable() {
            @Override
            public void draw(Graphics g, int x, int y) {
                Color bgColor = Color.WHITE;
                Graphics gr = image.getGraphics();
                gr.setColor(bgColor);
                gr.drawString("Game over", w >> 1, h >> 1);
            }
        });
    }

    private static void shutdown() {
        log.info("shutting down");
        Thread thread = RenderEngine.getInstance().stop();
        try {
            if (thread.isAlive()) {
                log.info("joining to the thread...");
                thread.join();
                log.info("join released...");
            }
        } catch (InterruptedException e1) {
            log.warn("join interrupted.", e1);
        }
    }

    private static void initRenderCallback() {
        RenderEngine renderEngine = RenderEngine.getInstance();
        renderEngine.addListener(drawables -> {
            reset();
            Graphics graphics = image.getGraphics();
            synchronized (drawables) {
                for (Drawable drawable : drawables) {
                    drawable.draw(graphics);
                }
            }
            render();
        });
    }


    private static void initComponents() {
        Dimension dimension = new Dimension(w, h);

        component.setMinimumSize(dimension);
        component.setMaximumSize(dimension);
        component.setPreferredSize(dimension);

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(component, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setBounds(150, 150, w, h);
        frame.setVisible(true);
    }

    private static void render() {
        component.getGraphics().drawImage(image, 0, 0, w, h, 0, 0, w, h, null);
    }

    private static void reset() {
        Color bgColor = Color.BLACK;
        Graphics gr = image.getGraphics();
        gr.setColor(bgColor);
        gr.fillRect(0, 0, w, h);
    }



}
