package oov.tetris.draw.item;

import java.awt.*;
import java.util.Random;

/**
 * Created by Olegdelone on 02.08.2015.
 */
public class CompObjFactory {

    private static final Color[] COLORS = {Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA};
    private static final Random RANDOM = new Random();

    public enum PART {
        I, J, L, O, S, T, Z;

        static PART getRand() {
            return values()[RANDOM.nextInt(7)];
        }
    }

    public static Color getRandColor() {
        return COLORS[RANDOM.nextInt(COLORS.length)];
    }

    public static CompoundObj makeRandColorObj(int x, int y, int cellW, int cellH, PART part) {
        CompoundObj result;
        Color color = getRandColor();

        switch (part) {
            case I:
                result = new Iobj(x, y, color, cellW, cellH);
                break;
            case J:
                result = new Jobj(x, y, color, cellW, cellH);
                break;
            case L:
                result = new Lobj(x, y, color, cellW, cellH);
                break;
            case O:
                result = new Oobj(x, y, color, cellW, cellH);
                break;
            case S:
                result = new Sobj(x, y, color, cellW, cellH);
                break;
            case T:
                result = new Tobj(x, y, color, cellW, cellH);
                break;
            case Z:
                result = new Zobj(x, y, color, cellW, cellH);
                break;
            default:
                throw new RuntimeException("something wrong with factory method");
        }
        return result;

    }

    public static CompoundObj makeRandObj(int x, int y, int cellW, int cellH) {
        PART p = PART.getRand();
//        PART p = PART.I;
        return makeRandColorObj(x, y, cellW, cellH, p);
    }
}
