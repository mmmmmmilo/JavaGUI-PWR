import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrafficPanel extends JPanel implements Runnable {
    private CopyOnWriteArrayList<Car> cars;
    private TrafficLightController trafficLightController;
    private Random random;
    private Color darkGreen = new Color(8, 96, 48);

    public TrafficPanel() {
        cars = new CopyOnWriteArrayList<>();
        trafficLightController = new TrafficLightController();
        random = new Random();
        new Timer(720, e -> spawnCar()).start();
        new Thread(() -> trafficLightController.update()).start();
    }

    private void spawnCar() {
        Car car = new Car(cars, trafficLightController);
        cars.add(car);
        new Thread(car::update).start();
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(darkGreen);
        drawIntersection(g);
        trafficLightController.draw(g);
        for (Car car : cars) {
            car.draw(g);
        }
    }

    private void drawIntersection(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(300, 0, 200, 800);
        g.fillRect(0, 300, 800, 200);

        g.setColor(Color.WHITE);
        g.fillRect(398, 0, 4, 800);
        g.fillRect(0, 398, 800, 4);

        g.setColor(Color.WHITE);
        for (int i = 0; i < 800; i += 40) {
            g.fillRect(348, i, 4, 20);
            g.fillRect(448, i, 4, 20);
            g.fillRect(i, 348, 20, 4);
            g.fillRect(i, 448, 20, 4);
        }

        g.setColor(Color.GRAY);
        g.fillRect(300, 300, 200, 200);
    }

    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
