package oov.tetris.proc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olegdelone on 16.08.2015.
 */
class MyMap extends HashMap {
    static int num = 0;
    private int n = num++;
    public MyMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MyMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MyMap() {
    }

    public MyMap(Map m) {
        super(m);
    }

    @Override
    public String toString() {
        return "MAP: " + n;
    }
}
