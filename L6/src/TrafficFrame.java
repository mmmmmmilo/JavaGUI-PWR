import javax.swing.*;

public class TrafficFrame extends JFrame {
    private TrafficPanel trafficPanel;

    public TrafficFrame() {
        setTitle("Traffic Intersection Simulation");
        setSize(800, 800);
        trafficPanel = new TrafficPanel();
        add(trafficPanel);
        new Thread(trafficPanel).start();
    }
}
