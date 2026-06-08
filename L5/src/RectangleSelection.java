import javax.swing.*;
import java.awt.*;

public class RectangleSelection {
    private Rectangle selection;
    private Point start;
    boolean isSelecting = false;

    public RectangleSelection() {
        selection = new Rectangle(0, 0, 0, 0);
    }

    public void mousePressed(Point setStart) {
        start = setStart;
        selection.x = setStart.x;
        selection.y = setStart.y;
        selection.width = 0;
        selection.height = 0;
        isSelecting = true;
    }

    public Zaznaczenie mouseReleased(JPanel drawPanel) {
        isSelecting = false;
        if (selection.width == 0 || selection.height == 0) {
            return null;
        }
        return new Zaznaczenie(selection.x, selection.y, selection.width, selection.height);
    }

    public void mouseMoved(Point end, JPanel drawPanel) {
        if (!isSelecting) {
            return;
        }
        selection.x = Math.min(start.x, end.x);
        selection.y = Math.min(start.y, end.y);
        selection.width = Math.abs(start.x - end.x);
        selection.height = Math.abs(start.y - end.y);
        drawPanel.repaint();
    }

    public void draw(Graphics2D g) {
        if (!isSelecting) {
            return;
        }
        g.setColor(Color.GREEN);
        g.draw(selection);
    }
}
