import java.util.ArrayList;
import java.util.List;


public class Function {
    public enum FType {
        LINEAR, QUADRATIC, SINE;
    }

    public FType type;
    public double a, b, c;
    public double ymin, ymax;
    public List<Double> xValues;
    public List<Double> yValues;

    public Function(FType setType, double setA, double setB, double setC) {
        setFunction(setType, setA, setB, setC);
    }

    public void setFunction(FType setType, double setA, double setB, double setC) {
        type = setType;
        a = setA;
        b = setB;
        c = setC;
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    public void calculateValues(double xmin, double xmax, int k) {
        xValues.clear();
        yValues.clear();
        double step = (xmax - xmin) / (k - 1);
        ymin = switch (type) {
            case LINEAR -> a * xmin + b;
            case QUADRATIC -> a * xmin * xmin + b * xmin + c;
            case SINE -> a * Math.sin(xmin - b * Math.PI) + c;
        };
        ymax = ymin;
        for (int i = 0; i < k; i++) {
            double x = xmin + i * step;
            double y = switch (type) {
                case LINEAR -> a * x + b;
                case QUADRATIC -> a * x * x + b * x + c;
                case SINE -> a * Math.sin(x - b * Math.PI) + c;
            };
            if (y < ymin)
            {
                ymin = y;
            }
            else if (y > ymax)
            {
                ymax = y;
            }
            xValues.add(x);
            yValues.add(y);
        }
    }

    @Override
    public String toString() {
        return switch (type) {
            case LINEAR -> "y = " + a + "x + " + b;
            case QUADRATIC -> "y = " + a + "x^2 + " + b + "x + " + c;
            case SINE -> "y = " + a + "sin(x - " + b + "pi) + " + c;
        };
    }
}