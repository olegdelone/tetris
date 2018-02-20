package oov.tetris.proc;

public class RowBunch {
    final int startY;
    final int cnt;
    public RowBunch(int startY, int cnt) {
        this.startY = startY;
        this.cnt = cnt;
    }

    public int getStartY() {
        return startY;
    }

    public int getCnt() {
        return cnt;
    }

    @Override
    public String toString() {
        return "RowBunch{" +
                "startY=" + startY +
                ", cnt=" + cnt +
                '}';
    }
}
