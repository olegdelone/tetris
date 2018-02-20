package oov.tetris;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Ascii {
    private static Logger log = LoggerFactory.getLogger(Ascii.class);

    @Test
    public void print(){
        for (int i = 0; i < 1000; i++) {
            char c = (char)i;
//            System.out.println("i = "+i+"; c = " + c);
            log.debug("i={}; c={};", i, c);

        }
        log.error("exc", new Exception("e"));
    }

}
