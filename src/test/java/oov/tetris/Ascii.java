package oov.tetris;

//import org.junit.Test;

import oov.tetris.proc.RenderEngine;
import oov.tetris.util.Logger;
import org.junit.Test;

/**
 * ========== ItCorp v. 1.0 class library ==========
 * <p/>
 * http://www.it.ru/
 * <p/>
 * &copy; Copyright 1990-2013, by ItCorp.
 * <p/>
 * ========== Ascii.java ==========
 * <p/>
 * $Revision: 47 $<br/>
 * $Author: Olezha $<br/>
 * $HeadURL: file:///D:/work/local_repository/tetris/trunk/src/test/java/oov/tetris/Ascii.java $<br/>
 * $Id: Ascii.java 47 2014-07-11 12:25:33Z Olezha $
 * <p/>
 * 07.09.13 6:33: Original version (OOBUKHOV)<br/>
 */
public class Ascii {
    private static transient Logger log = Logger.getLogger(Ascii.class);

    @Test
    public void print(){
        for (int i = 0; i < 1000; i++) {
            char c = (char)i;
//            System.out.println("i = "+i+"; c = " + c);
            log.debug("i={}; c={};", i, c);

        }
        log.error("exc", new Exception("e"));
    }
    @Test
    public void compactPackage(){
        String r = Logger.compactPackage((short) 8, (short) 8, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 8, (short) 9, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 8, (short) 10, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 10, (short) 12, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 10, (short) 15, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 30, (short) 35, OverriddenComponent.class);
        log.info("r = {} ", r);
    }

    public void ttt(){
        String r = Logger.compactPackage((short) 8, (short) 8, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 8, (short) 9, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 8, (short) 10, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 10, (short) 12, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 10, (short) 15, OverriddenComponent.class);
        log.info("r = {} ", r);
        r = Logger.compactPackage((short) 30, (short) 35, OverriddenComponent.class);
        log.info("r = {} ", r);
    }
}
