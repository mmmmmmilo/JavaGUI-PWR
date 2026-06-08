import java.awt.*;

public class Zaznaczenie {
    public Rectangle selection;
    int X, Y, W, H;

    public Zaznaczenie(int x, int y, int w, int h) {
        X = x;
        Y = y;
        W = w;
        H = h;
        selection = new Rectangle(x, y, w, h);
    }
}
