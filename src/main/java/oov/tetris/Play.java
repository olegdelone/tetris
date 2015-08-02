package oov.tetris;

import oov.tetris.draw.Drawable;
import oov.tetris.proc.RenderEngine;
import oov.tetris.proc.GameController;
import oov.tetris.util.AppProperties;
import oov.tetris.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * 07.09.13 18:13: Original version (OOBUKHOV)<br/>
 */
public class Play {

    private static transient Logger log = Logger.getLogger(Play.class);

    public static final int w = Integer.valueOf(AppProperties.get("canvas.width"));
    public static final int h = Integer.valueOf(AppProperties.get("canvas.height"));

    public final static BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    public final static JComponent component = new OverriddenComponent();

    public static void main(String[] args) {
        initComponents();
        initRenderCallback();

        final GameController gameController = new GameController();

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    int keyCode = e.getKeyCode();
//                    log.info("key = {}", keyCode);
                    try {
                        gameController.setAction(keyCode);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                return true;
            }
        });
//        reset();
//        render();


        try {
            gameController.run();
        } catch (IllegalStateException e) {
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
            RenderEngine.getInstance().fullRender();
            log.error("e: ", e);
            Color bgColor = Color.WHITE;
            Graphics gr = image.getGraphics();
            gr.setColor(bgColor);
            gr.drawString("Game over", w >> 1, h >> 1);
            render();
        }

    }

    private static void initRenderCallback() {
        RenderEngine renderEngine = RenderEngine.getInstance();
        renderEngine.addListener(new RenderEngine.RenderListener() {
            @Override
            public void onEvent(Collection<Drawable> drawables) {
                reset();
                Graphics graphics = image.getGraphics();
                synchronized (drawables){
                    for (Drawable drawable : drawables) {
                        drawable.draw(graphics);
                    }
                }
                render();
            }
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
