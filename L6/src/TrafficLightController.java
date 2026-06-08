import java.awt.*;

public class TrafficLightController {
    private TrafficLight[] trafficLights;
    public boolean isVerticalGreen;

    public TrafficLightController() {
        isVerticalGreen = true;
        trafficLights = new TrafficLight[8];
        trafficLights[0] = new TrafficLight(290, 405, false);
        trafficLights[1] = new TrafficLight(290, 455, false);
        trafficLights[2] = new TrafficLight(500, 305, false);
        trafficLights[3] = new TrafficLight(500, 355, false);
        trafficLights[4] = new TrafficLight(305, 290, true);
        trafficLights[5] = new TrafficLight(355, 290, true);
        trafficLights[6] = new TrafficLight(405, 500, true);
        trafficLights[7] = new TrafficLight(455, 500, true);
    }

    public void update() {
        while (true) {
            try {
                isVerticalGreen = !isVerticalGreen;
                for (int i = 0; i < 8; i++) {
                    trafficLights[i].setGreen(!trafficLights[i].isGreen());
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < 8; i++) {
            trafficLights[i].draw(g);
        }
    }
}
