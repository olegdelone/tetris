package oov.tetris.proc;

import oov.tetris.Play;
import oov.tetris.draw.Drawable;
import oov.tetris.util.Logger;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Olezha
 * Date: 11.07.14
 * Time: 0:51
 * To change this template use File | Settings | File Templates.
 */
public class RenderEngine implements Runnable {
    private static transient Logger log = Logger.getLogger(RenderEngine.class);
    private final static int RENDER_TIME = 1000 / 25;
    private final Thread thread;
    private static volatile RenderEngine instance;
    private final Collection<RenderListener> renderListeners = new ArrayList<RenderListener>();
    private final Collection<Drawable> drawables = new ArrayList<Drawable>();

    public void addListener(RenderListener renderListener) {
        renderListeners.add(renderListener);
    }

    private RenderEngine() {
        thread = new Thread(this);
    }

    public boolean add(Drawable drawable) {
        synchronized (drawables) {
            return drawables.add(drawable);
        }
    }

    public boolean remove(Drawable drawable) {
        return drawables.remove(drawable);
    }

    public Thread stop() {
        if (thread == null) {
            throw new IllegalStateException("thread not started");
        }
        thread.interrupt();
        instance = null;
        return thread;
    }

    public static RenderEngine getInstance() {
        if (instance == null) {
            synchronized (RenderEngine.class) {
                if (instance == null) {
                    instance = new RenderEngine();
                    instance.start();
                }
            }
        }
        return instance;
    }

    private void start() {
        thread.start();
    }

    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            long timeCurrent = System.currentTimeMillis();
            if (timeCurrent - timeStart >= RENDER_TIME) {
                fullRender();
                long operationTime = System.currentTimeMillis() - timeCurrent;
                long freeTime = RENDER_TIME - operationTime;
                if (freeTime > 10) {
                    try {
                        Thread.sleep(freeTime);
                    } catch (InterruptedException e) {
                        log.info("Renderer interrupted...");
                        break;
                    }
                }
                timeStart = timeCurrent;
            }
        }
    }

    public void fullRender() {
        notifyListeners(drawables);
    }

    private void notifyListeners(Collection<Drawable> drawables) {
        for (RenderListener renderListener : renderListeners) {
            renderListener.onEvent(drawables);
        }
    }

    public interface RenderListener {
        void onEvent(Collection<Drawable> drawables);
    }
}
