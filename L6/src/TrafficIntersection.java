import javax.swing.*;

public class TrafficIntersection {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrafficFrame frame = new TrafficFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
