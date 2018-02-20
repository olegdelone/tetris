package oov.tetris;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JndiTest {
    private static Logger log = LoggerFactory.getLogger(JndiTest.class);

    class A{};
    class B extends A{};
    class C extends  B{};
    class A1{};
    class B1 extends A1{};
    class C1 extends  B1{};

    class AA{
        B b;
        B1 b1;

        public void setB(B b) {
            log.info("AA.setB: {}", b);
            this.b = b;
        }
        public void setB(A a) {
            log.info("AA.setB a: {}", a);
            this.b = (B)a;
        }
        public void setB(C c) {
            log.info("AA.setB c: {}", c);
            this.b = c;
        }

        public void setB(Integer b) {
            log.info("AA.setB: {}", b);
        }

//        public void setB(C1 c) {
//            log.info("AA.setB c: {}", c);
//            this.b1 = c;
//        }
    }





    @Test
    public void print() {

    }

}
