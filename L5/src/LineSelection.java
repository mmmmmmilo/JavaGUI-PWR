import javax.swing.*;
import java.awt.*;

public class LineSelection {
    private int vLine1x = 100, vLine2x = 800, hLine1y = 100, hLine2y = 450;
    private boolean vLine1b = false, vLine2b = false, hLine1b = false, hLine2b = false;

    public void mousePressed(Point p) {
        if (Math.abs(p.getX() - vLine1x) < 5) {
            vLine1b = true;
        }
        else if (Math.abs(p.getX() - vLine2x) < 5) {
            vLine2b = true;
        }
        if (Math.abs(p.getY() - hLine1y) < 5) {
            hLine1b = true;
        }
        else if (Math.abs(p.getY() - hLine2y) < 5) {
            hLine2b = true;
        }
    }

    public Zaznaczenie mouseReleased() {
        vLine1b = false;
        vLine2b = false;
        hLine1b = false;
        hLine2b = false;

        if (vLine1x == vLine2x || hLine1y == hLine2y) {
            return null;
        }
        return new Zaznaczenie(Math.min(vLine1x, vLine2x), Math.min(hLine1y, hLine2y), Math.abs(vLine1x - vLine2x), Math.abs(hLine1y - hLine2y));
    }

    public void mouseMoved(Point p, JPanel drawPanel) {
        if (vLine1b) {
            vLine1x = (int)p.getX();
        }
        if (vLine2b) {
            vLine2x = (int)p.getX();
        }
        if (hLine1b) {
            hLine1y = (int)p.getY();
        }
        if (hLine2b) {
            hLine2y = (int)p.getY();
        }
        drawPanel.repaint();
    }

    public void draw(Graphics2D g, JPanel drawPanel) {
        g.setColor(Color.BLUE);
        float[] dashPattern = {5, 5};
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        g.drawLine(vLine1x, 0, vLine1x, drawPanel.getHeight());
        g.drawLine(vLine2x, 0, vLine2x, drawPanel.getHeight());
        g.drawLine(0, hLine1y, drawPanel.getWidth(), hLine1y);
        g.drawLine(0, hLine2y, drawPanel.getWidth(), hLine2y);
    }
}
