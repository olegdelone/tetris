package oov.tetris.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Olegdelone on 19.07.2015.
 */
public class Logger {
    private static transient Logger log = Logger.getLogger(Logger.class);
    public static Logger getLogger(Class aClass) {
        return new Logger(aClass);
    }

    String lineSeparator = java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));

    private static final String pattern = "yyyy-MM-dd HH:mm:ss.S";
    private static final ThreadLocal<SimpleDateFormat> formatters =
            new ThreadLocal<SimpleDateFormat>() {
                @Override
                public SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(pattern);
                }
            };

    private static final Level level;

    private Class clazz;

    static {
        String prop;
        Level l = Level.DEBUG;
        if ((prop = AppProperties.get("log.level")) != null) {
            l = Level.valueOf(prop.toUpperCase());
        }
        level = l;
//        log(Level.INFO, Logger.class, "Level is set: {}", level);
        log.info("Level is set: {}", level);
    }

    enum Level {
        DEBUG(1), INFO(2), WARNING(3), ERROR(4);

        private final int ordinal;

        Level(int ordinal) {
            this.ordinal = ordinal;
        }

        public static boolean checkLevel(Level l) {
            return l.ordinal >= level.ordinal;
        }
    }

    private Logger(Class aClass) {
        clazz = aClass;
    }

    private static void log(Level lvl, Class clazz, String msg, Object... objects) {
        if (objects.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object object : objects) {
                int f;
                if (msg.indexOf('}') - (f = msg.indexOf('{')) == 1 && f != -1) {
                    stringBuilder.append(msg.substring(0, f));
                    stringBuilder.append(String.valueOf(object));
                    msg = msg.substring(f + 2);
                }
            }
            msg = stringBuilder.append(msg).toString();
        }
        StringBuilder stringBuilder = new StringBuilder(lvl.toString()).append(" ").append(formatters.get().format(new Date()))
                .append("[").append(Thread.currentThread().getName()).append("]").append(compactPackage(20, 50, clazz)).append(": ")
                .append(msg);
        System.out.println(stringBuilder.toString());
    }

    public static String compactPackage(int firstLimit, int secondLimit, Class clazz) {
        String result = clazz.getName();
        int rem = 0;
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while (result.length() + rem > firstLimit && (i = result.indexOf('.')) != -1) {
            String chunk = result.substring(0, i);
            rem += 2;
            stringBuilder.append(chunk.charAt(0)).append('.');
            result = result.substring(i + 1);
        }
        result = stringBuilder.append(result).toString();
        int origL = result.length();
        int over = origL - secondLimit;

        int li = result.lastIndexOf('.');
        if(li ==  -1){
            li = 0;
        }
        String rep = "..";
        int repL = rep.length();
        if(over > 0 ){
            if((origL - li) <= over + repL + 4){
                over = origL - li - repL - 5;
            }
            int middlePos = ((origL - li) >> 1) + li;
            int side = (over + repL) >> 1;
            result = result.substring(0, middlePos - side) + rep + result.substring(middlePos + side);
        }
        return result;
    }

    public void debug(String msg, Object... objects) {
        if (isDebugEnabled()) {
            log(Level.DEBUG, clazz, msg, objects);
        }
    }

    public void info(String msg, Object... objects) {
        if (isInfoEnabled()) {
            log(Level.INFO, clazz, msg, objects);
        }
    }

    public void warn(String msg, Object... objects) {
        if (isWarningEnabled()) {
            log(Level.WARNING, clazz, msg, objects);
        }
    }

    public void error(String msg, Object... objects) {
        if (isErrorEnabled()) {
            log(Level.ERROR, clazz, msg, objects);
        }
    }

    public void error(String msg, Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter w = new PrintWriter(sw);
        t.printStackTrace(w);
        error(msg + lineSeparator + sw.toString());
        try {
            sw.close();
        } catch (IOException e) {
            warn("Exception arisen while closing stringWriter: {}", e.toString());
        }
        w.close();
    }

    public boolean isDebugEnabled() {
        return Level.checkLevel(Level.DEBUG);
    }

    public boolean isInfoEnabled() {
        return Level.checkLevel(Level.INFO);
    }

    public boolean isWarningEnabled() {
        return Level.checkLevel(Level.WARNING);
    }

    public boolean isErrorEnabled() {
        return Level.checkLevel(Level.ERROR);
    }


}
