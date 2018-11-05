package oov.tetris.draw.view;

import java.awt.*;

public class AnchorText extends AnchorControl {
    private String text;
    private Color bgColor;

    public AnchorText(String text, Color bgColor) {
        this.text = text;
        this.bgColor = bgColor;
    }

    @Override
    public void draw(Graphics g) {
        int x = getAnchor().x;
        int y = getAnchor().y;
        Color bgColor = Color.WHITE;
        g.setColor(bgColor);
        g.drawString("Game over", x, y);
    }
}
