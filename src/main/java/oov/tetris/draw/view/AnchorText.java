package oov.tetris.draw.view;

import java.awt.*;

public class AnchorText extends AncorControl {
    private String text;
    private Color bgColor;

    public AnchorText(String text, Color bgColor) {
        this.text = text;
        this.bgColor = bgColor;
    }

    @Override
    public void draw(Graphics g) {
        int x = getAncor().x;
        int y = getAncor().y;
        Color bgColor = Color.WHITE;
        g.setColor(bgColor);
        g.drawString("Game over", x, y);
    }
}
