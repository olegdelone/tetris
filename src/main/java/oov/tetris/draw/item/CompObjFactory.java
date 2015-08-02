package oov.tetris.draw.item;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Olegdelone on 02.08.2015.
 */
public class CompObjFactory {

    private static final Color[] COLORS = {Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA};
    private static final Random RANDOM = new Random();

    public static Color getRandColor() {
        return COLORS[RANDOM.nextInt(COLORS.length)];
    }
//    public final static Class<? extends CompoundObj>[] TYPES = new Class[]{Iobj.class, Jobj.class, Lobj.class, Oobj.class, Sobj.class, Tobj.class, Zobj.class};


    public static CompoundObj makeRandObj(int x, int y, int cellW, int cellH){
        CompoundObj result;
        Color color = getRandColor();
        switch (RANDOM.nextInt(7)){
            case 0: result = new Iobj(x,y,color,cellW,cellH); break;
            case 1: result = new Jobj(x,y,color,cellW,cellH); break;
            case 2: result = new Lobj(x,y,color,cellW,cellH); break;
            case 3: result = new Oobj(x,y,color,cellW,cellH); break;
            case 4: result = new Sobj(x,y,color,cellW,cellH); break;
            case 5: result = new Tobj(x,y,color,cellW,cellH); break;
            case 6: result = new Zobj(x,y,color,cellW,cellH); break;
            default: throw new RuntimeException("something wrong with factory method");
        }
        return result;
    }
}
