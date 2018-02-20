package oov.tetris.proc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import oov.tetris.draw.Drawable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


public class RenderEngine implements Runnable {
    private static Logger log = LoggerFactory.getLogger(RenderEngine.class);

    private static volatile RenderEngine instance;
    private final static int SECOND = 1000;
    private final static int FPS_TIME = SECOND / 5;
    private final static int RENDER_TIME = SECOND / 25;
    private final Thread thread;
    private FpsListener fpsListener;
    private final Collection<RenderListener> renderListeners = Lists.newArrayList();
    private final Collection<Drawable> drawables = Lists.newArrayList();

    public void addListener(RenderListener renderListener) {
        renderListeners.add(renderListener);
    }

    private RenderEngine() {
        thread = new Thread(this);
        thread.start();
    }

    public boolean add(Drawable drawable) {
        synchronized (drawables) {
            return drawables.add(drawable);
        }
    }

    public boolean removeAll(Collection<? extends Drawable> drawable) {
        synchronized (drawables) {
            return drawables.removeAll(drawable);
        }
    }

    public Thread stop() {
        Preconditions.checkState(thread != null);
        thread.interrupt();
        instance = null;
        try {
            if (thread.isAlive()) {
                log.info("joining to the thread...");
                thread.join();
                log.info("join released...");
            }
        } catch (InterruptedException e1) {
            log.warn("join interrupted.", e1);
        }
        return thread;
    }

    public static RenderEngine getInstance() {
        if (instance == null) {
            synchronized (RenderEngine.class) {
                if (instance == null) {
                    instance = new RenderEngine();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();
        long timeStamp = timeStart;
        int cnt = 0;
        while (!Thread.currentThread().isInterrupted()) {
            long timeCurrent = System.currentTimeMillis();

            if (timeCurrent - timeStamp >= FPS_TIME) {
                float fps = SECOND / ((timeCurrent - timeStamp)/cnt);
                if(fpsListener != null){
                    fpsListener.onEvent(fps);
                }
                cnt = 0;
                timeStamp = timeCurrent;
            }
            if (timeCurrent - timeStart >= RENDER_TIME) {
                cnt++;
                fullRender();
                long operationTime = System.currentTimeMillis() - timeCurrent;
                long freeTime = RENDER_TIME - operationTime;
                if (freeTime > 20) {
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

    public void setFpsListener(FpsListener fpsListener) {
        this.fpsListener = fpsListener;
    }


    public interface RenderListener {
        void onEvent(Collection<Drawable> drawables);
    }

    public interface FpsListener {
        void onEvent(float fps);
    }

    private void fullRender() {
        notifyListeners(drawables);
    }

    private void notifyListeners(Collection<Drawable> drawables) {
        for (RenderListener renderListener : renderListeners) {
            renderListener.onEvent(drawables);
        }
    }

}
