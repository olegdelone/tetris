package oov.tetris.proc;

import com.google.common.base.Preconditions;

public class Rule {

    public int msUponLevel(int level){
        Preconditions.checkArgument(level <= 10 && level >= 1);
        return 300 - (level-1)*30;
    }

    public int levelUponScores(int scores){
        int level = scores/1000 + 1;
        if(level > 10){
            level = 10;
        }
        return level;
    }

}
