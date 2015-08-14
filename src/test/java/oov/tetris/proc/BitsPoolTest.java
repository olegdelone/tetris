package oov.tetris.proc;

import oov.tetris.draw.BoxPoint;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.util.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olegdelone on 14.08.2015.
 */
public class BitsPoolTest {
    private static transient Logger log = Logger.getLogger(BitsPoolTest.class);

    @Test
    public void pt1(){
        List<Integer> filled = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(6);
            add(9);
        }};

        int yCap = 10, xCap = 10;
        BitsPool bitsPool = new BitsPool(xCap,yCap);
        for (int i = 0; i < yCap; i++) {
            if(filled.contains(i)){
                for (int j = 0; j < xCap; j++) {
                    bitsPool.put(new BoxPoint(j, i, null, null, 0, 0));
                }
            } else {
                bitsPool.put(new BoxPoint(i, i, null, null, 0, 0));
            }
        }
        log.info("{}", bitsPool);
        //-------------------------

        CompoundObj compoundObj = CompObjFactory.makeRandColorObj(6,5,10,10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        Assert.assertTrue(!bitsPool.checkInPool(compoundObj));

        compoundObj = CompObjFactory.makeRandColorObj(0,8,10,10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        Assert.assertTrue(!bitsPool.checkInPool(compoundObj));


        compoundObj = CompObjFactory.makeRandColorObj(4,5,10,10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);
        Assert.assertTrue(bitsPool.checkInPool(compoundObj));


        compoundObj = CompObjFactory.makeRandColorObj(5,8,10,10, CompObjFactory.PART.Z);
        compoundObj.rotateCCW();
        log.info("{}", compoundObj);
        Assert.assertTrue(!bitsPool.checkInPool(compoundObj));

        Assert.assertTrue(bitsPool.checkGapsClash(compoundObj.getxGap(),
                compoundObj.getyGap(),
                compoundObj.getCursor().getX(),
                compoundObj.getCursor().getY()));

        Assert.assertTrue(bitsPool.checkInPool(0,0));
        Assert.assertTrue(!bitsPool.checkInPool(1,0));



    }

    @Test
    public void pt2(){


        int yCap = 10, xCap = 10;
        BitsPool bitsPool = new BitsPool(xCap,yCap);

        //-------------------------

        CompoundObj compoundObj = CompObjFactory.makeRandColorObj(3,6,10,10, CompObjFactory.PART.S);
        log.info("{}", compoundObj);

        bitsPool.put(compoundObj);
        log.info("{}", bitsPool);

    }

}
