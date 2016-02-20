package oov.tetris.util;

import java.util.HashMap;
import java.util.Map;


public class ScoresUtils {

    private static Map<Integer, Integer> CACHE_ = new HashMap<>(4);

    public static int calcAndGetScores(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException("arg a<=0");
        }
        Integer scores = CACHE_.get(a);
        if(scores == null){
            CACHE_.put(a, scores = calcScores(a));
        }

        return scores;
    }

    private static int calcScores(int a) {
        if (a == 1) {
            return 100;
        }
        return 2 * calcScores(--a) + 100;
    }
}
