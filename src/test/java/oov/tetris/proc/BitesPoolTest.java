package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitesPoolTest {

    private static Logger log = LoggerFactory.getLogger(BitesPoolTest.class);

    @Test
    public void pt1() {
        List<Integer> filled = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(6);
            add(9);
        }};

        int yCap = 10, xCap = 10;
        BitesPool bitesPool = new BitesPool(xCap, yCap);
        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bitesPool.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            } else {
                bitesPool.put(new BoxPoint(i, i, null, null, 0, 0));
            }
        }
        log.info("{}", bitesPool);
        //-------------------------

        CompoundObj compoundObj = CompObjFactory.makeRandColorObj(6, 5, 10, 10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        Assert.assertTrue(!bitesPool.checkInPool(compoundObj));

        compoundObj = CompObjFactory.makeRandColorObj(0, 8, 10, 10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        Assert.assertTrue(!bitesPool.checkInPool(compoundObj));


        compoundObj = CompObjFactory.makeRandColorObj(4, 5, 10, 10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);
        Assert.assertTrue(bitesPool.checkInPool(compoundObj));


        compoundObj = CompObjFactory.makeRandColorObj(5, 8, 10, 10, CompObjFactory.PART.Z);
        compoundObj.rotateCCW();
        log.info("{}", compoundObj);
        Assert.assertTrue(!bitesPool.checkInPool(compoundObj));

        Assert.assertTrue(bitesPool.checkGapsClash(compoundObj.getxGap(),
                compoundObj.getyGap(),
                compoundObj.getCursor().getX(),
                compoundObj.getCursor().getY()));

        Assert.assertTrue(bitesPool.checkInPool(0, 0));
        Assert.assertTrue(!bitesPool.checkInPool(1, 0));

        bitesPool.eraseLines();
        log.info("{}", bitesPool);
    }

    @Test
    public void pt2() {


        int yCap = 10, xCap = 10;
        BitesPool bitesPool = new BitesPool(xCap, yCap);

        //-------------------------

        CompoundObj compoundObj = CompObjFactory.makeRandColorObj(3, 6, 10, 10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        bitesPool.put(compoundObj);
        log.info("{}", bitesPool);

    }

    @Test
    public void pt3() {

        List<Integer> filled = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(6);
            add(9);
        }};

        int yCap = 10, xCap = 10;
        BitesPool bitesPool = new BitesPool(xCap, yCap);
        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bitesPool.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            } else {
//                bitesPool.put(new BoxPoint(i, i, null, null, 0, 0));
            }
        }
        log.info("before {}", bitesPool);

        Map<Integer, Map<Integer, BoxPoint>> snapshot = new HashMap<Integer, Map<Integer, BoxPoint>>(10);
        for (int i = 0; i < 10; i++) {
            snapshot.put(i, bitesPool.getRow(i));
        }
        Map<Integer, BoxPoint> y0 = bitesPool.getRow(0);
        bitesPool.moveTopPartDown(7, 1);
        log.info("after {}", bitesPool);
        for (int i = 0; i < 10; i++) {
            if (i >= 1 && i <= 7) {
                Assert.assertTrue(snapshot.get(i - 1) == bitesPool.getRow(i));
            } else {
                Assert.assertTrue(snapshot.get(i) == bitesPool.getRow(i));
            }
        }
        //-------------------------

    }


    @Test
    public void pt4() {

        List<Integer> filled = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(6);
            add(9);
        }};

        int yCap = 10, xCap = 10;
        BitesPool bitesPool = new BitesPool(xCap, yCap);
        for (int i = 0; i < yCap; i++) {
            bitesPool.put(new BoxPoint(i, i, null, null, 0, 0));
        }
        bitesPool.reset();

        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bitesPool.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            }
        }
        log.info("before {}", bitesPool);

        Map<Integer, Map<Integer, BoxPoint>> snapshot = new HashMap<Integer, Map<Integer, BoxPoint>>(10);
        for (int i = 0; i < yCap; i++) {
            snapshot.put(i, bitesPool.getRow(i));
        }
        Map<Integer, BoxPoint> y0 = bitesPool.getRow(0);
        bitesPool.moveTopPartDown(4, 2);
        log.info("after {}", bitesPool);
        for (int i = 0; i <= 3; i++) {
                Assert.assertTrue(snapshot.get(i) == bitesPool.getRow(i+2));
        }

        for (int i = 6; i <= yCap; i++) {
                Assert.assertTrue(snapshot.get(i) == bitesPool.getRow(i));
        }
        Assert.assertTrue(snapshot.get(4) == bitesPool.getRow(1));
        Assert.assertTrue(snapshot.get(5) == bitesPool.getRow(0));
        //-------------------------

    }

    @Test
    public void pt5() {
        List<Integer> filled = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(5);
            add(6);
            add(9);
        }};
        int yCap = 10, xCap = 10;

        BPool1 bPool1 = new BPool1(xCap,yCap);
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        bPool1.setM(m);
        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bPool1.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            }
        }
        log.debug("pool: {}", bPool1);


        bPool1.eraseLines();
        log.info("map: {}", m);

        Assert.assertTrue(m.get(1) == 3);
        Assert.assertTrue(m.get(5) == 2);
        Assert.assertTrue(m.get(9) == 1);
    }


    @Test
    public void pt6() {
        List<Integer> filled = new ArrayList<Integer>() {{
            add(2);
            add(3);
            add(6);
            add(8);
            add(9);
        }};
        int yCap = 10, xCap = 10;

        BPool1 bPool1 = new BPool1(xCap,yCap);
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        bPool1.setM(m);

        for (int i = 0; i < yCap; i++) {
            bPool1.put(new BoxPoint(i, i, null, null, 0, 0));
        }
        bPool1.reset();

        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bPool1.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            }
        }
        log.debug("pool: {}", bPool1);


        bPool1.eraseLines();
        log.info("map: {}", m);

        Assert.assertTrue(m.get(2) == 2);
        Assert.assertTrue(m.get(6) == 1);
        Assert.assertTrue(m.get(8) == 2);
    }


    @Test
    public void pt7() {
        List<Integer> filled = new ArrayList<Integer>() {{
            add(2);
            add(3);
            add(6);
            add(8);
            add(9);
        }};
        int yCap = 10, xCap = 10;

        BitesPool bPool1 = new BitesPool(xCap,yCap);
        for (int i = 0; i < yCap; i++) {
            if (filled.contains(i)) {
                for (int j = 0; j < xCap; j++) {
                    bPool1.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            }
        }
        log.debug("before pool: {}", bPool1);


        bPool1.eraseLines();

        log.debug("after pool: {}", bPool1);

    }
}
