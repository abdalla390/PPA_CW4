import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class Spike extends Obstacle {
    public Spike(double x, double y) {
        super(x, y, 30, 30, true);
    }

    @Override
    public void update(double deltaTime) {
        // Static obstacle
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        gc.setFill(Color.web("#A69185")); // Gray-brown
        gc.fillPolygon(
            new double[]{x, x + width / 2, x + width},
            new double[]{y + height, y, y + height},
            3
        );
    }
}