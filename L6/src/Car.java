import java.awt.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Car {
    private int x, y;
    private int direction;
    private int lane0R1L;
    private boolean isTurning;
    private int speed;
    public int baseSpeed;
    private int[] lightsPos = {275, 505};
    private int[] turnLanes = {319, 419, 469, 369};
    private int color;
    private Color[] carColors = {Color.ORANGE, Color.CYAN, Color.YELLOW, Color.BLACK, Color.WHITE, Color.LIGHT_GRAY};
    private CopyOnWriteArrayList<Car> cars;
    private TrafficLightController tLC;

    public Car(CopyOnWriteArrayList<Car> setCars, TrafficLightController setTLC) {
        Random random = new Random();
        direction = random.nextInt(4);
        lane0R1L = random.nextInt(2);
        isTurning = random.nextInt(2) == 0;
        color = random.nextInt(6);
        baseSpeed = random.nextInt(3) + 3;
        speed = baseSpeed;
        cars = setCars;
        tLC = setTLC;
        switch (direction) {
            case 0 -> {
                x = lane0R1L == 0 ? 319 : 369; y = 0;
            }
            case 1 -> {
                x = lane0R1L == 0 ? 469 : 419; y = 800;
            }
            case 2 -> {
                x = 0; y = lane0R1L == 0 ? 469 : 419;
            }
            case 3 -> {
                x = 800; y = lane0R1L == 0 ? 319 : 369;
            }
        }
    }

    public void update() {
        while (true) {
            try {
                move();
                outOfBounds();
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        turn();
        adjustSpeed();
        if ((direction == 0 || direction == 1) && (!tLC.isVerticalGreen && Math.abs(y - lightsPos[direction % 2]) <= 15))
        {
            speed = 0;
        }
        else if ((direction == 2 || direction == 3) && (tLC.isVerticalGreen && Math.abs(x - lightsPos[direction % 2]) <= 15))
        {
            speed = 0;
        }

        if (speed > 0) {
            switch (direction) {
                case 0 -> y += speed;
                case 1 -> y -= speed;
                case 2 -> x += speed;
                case 3 -> x -= speed;
            }
        }
    }

    private void adjustSpeed() {
        speed = baseSpeed;
        for (Car car : cars) {
            if (car == this) continue;
            if (car.direction == this.direction && car.lane0R1L == this.lane0R1L) {
                if (direction == 0 && car.y > this.y && car.y - this.y < 40) {
                    speed = car.y - this.y <= 5 ? 0 : Math.min(speed, car.speed);
                } else if (direction == 1 && car.y < this.y && this.y - car.y < 40) {
                    speed = this.y - car.y <= 5 ? 0 : Math.min(speed, car.speed);
                } else if (direction == 2 && car.x > this.x && car.x - this.x < 40) {
                    speed = car.x - this.x <= 5 ? 0 : Math.min(speed, car.speed);
                } else if (direction == 3 && car.x < this.x && this.x - car.x < 40) {
                    speed = this.x - car.x <= 5 ? 0 : Math.min(speed, car.speed);
                }
            }
        }
    }

    private void turn() {
        if (isTurning && direction < 2 && Math.abs(y - turnLanes[direction * 2 + lane0R1L]) < 5) {
            if ((direction == 0 && lane0R1L == 0) || (direction == 1 && lane0R1L == 1)) {
                direction = 3;
            }
            else {
                direction = 2;
            }
            isTurning = false;
        }
        else if (isTurning && direction > 1 && Math.abs(x - turnLanes[(direction - 2) * 2 + lane0R1L]) < 5) {
            if ((direction == 2 && lane0R1L == 0) || (direction == 3 && lane0R1L == 1)) {
                direction = 0;
            }
            else {
                direction = 1;
            }
            isTurning = false;
        }
    }

    private void outOfBounds() {
        if (x < -20 || x > 800 || y < -20 || y > 800) {
            cars.remove(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(carColors[color]);
        if (direction < 2) {
            g.fillRect(x, y, 12, 20);
        }
        else {
            g.fillRect(x, y, 20, 12);
        }
    }
}
