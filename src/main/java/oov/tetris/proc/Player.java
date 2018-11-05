package oov.tetris.proc;

public class Player {
    private int scores;
    private int level;
    private boolean over;
    private PlayerInfoListener infoListener;

    public void setInfoListener(PlayerInfoListener infoListener) {
        this.infoListener = infoListener;
    }

    public void addScores(int scores) {
        this.scores += scores;
        if(infoListener != null) {
            infoListener.onScoresChanged(this.scores);
        }
    }

    public void setLevel(int level) {
        this.level = level;
        if(infoListener != null){
            infoListener.onLevelChanged(level);
        }
    }

    public int getScores() {
        return scores;
    }

    public int getLevel() {
        return level;
    }

//    public void incLevel() {
//        setLevel(level+1);
//    }

    public interface PlayerInfoListener{
        void onScoresChanged(int scores);
        void onLevelChanged(int level);
    }
}
