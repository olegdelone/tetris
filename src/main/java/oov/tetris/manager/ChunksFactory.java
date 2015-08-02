package oov.tetris.manager;

import oov.tetris.util.Logger;

import java.awt.*;
import java.util.Random;

/**
 * Created by Olegdelone on 22.07.2015.
 */
public final class ChunksFactory {
    private static transient Logger log = Logger.getLogger(ChunksFactory.class);

    private static final Color[] COLORS = {Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA};
    private static final Random RANDOM = new Random();

    public static Color getRandColor() {
        return COLORS[RANDOM.nextInt(COLORS.length)];
    }

//    public static BoxPoint makeRandColoredBoxPoint(int x, int y) {
//        return makeBoxPoint(x, y, getRandColor());
//    }


}
