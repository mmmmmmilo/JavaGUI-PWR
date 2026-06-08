import java.awt.*;

public class TrafficLight {
    private boolean isGreen;
    private boolean isVertical;
    private int x, y;

    public TrafficLight(int x, int y, boolean isVertical) {
        this.x = x;
        this.y = y;
        this.isVertical = isVertical;
        this.isGreen = isVertical;
    }

    public boolean isGreen() {
        return isGreen;
    }

    public void setGreen(boolean green) {
        isGreen = green;
    }

    public void draw(Graphics g) {
        g.setColor(isGreen ? Color.GREEN : Color.RED);
        if (isVertical) {
            g.fillRect(x, y, 40, 10);
        }
        else {
            g.fillRect(x, y, 10, 40);
        }
    }
}
