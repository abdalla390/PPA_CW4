import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Spike obstacle that damages the player on contact.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Spike extends Obstacle {
    /**
     * Creates a new spike obstacle at the specified position.
     * 
     * @param x The x-coordinate of the spike
     * @param y The y-coordinate of the spike
     */
    public Spike(double x, double y) {
        super(x, y, 30, 30, true);
    }

    /**
     * Updates the spike state (static obstacle).
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {
        // Static obstacle
    }

    /**
     * Renders the spike as a triangle.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        gc.setFill(Color.web("#D22F27")); 
        gc.fillPolygon(
            new double[]{x, x + width / 2, x + width},
            new double[]{y + height, y, y + height},
            3
        );
    }
}