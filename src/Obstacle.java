import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Abstract class representing obstacles in the game environment.
 * Obstacles can either damage the player or serve as platforms.
 * This class extends GameObject and provides a foundation for
 * specific obstacle types.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public abstract class Obstacle extends GameObject {
    /** Indicates whether this obstacle inflicts damage on contact with the player */
    protected boolean isDamaging;

    /**
     * Constructs a new obstacle with the specified properties.
     * 
     * @param x The x-coordinate of the obstacle
     * @param y The y-coordinate of the obstacle
     * @param width The width of the obstacle
     * @param height The height of the obstacle
     * @param isDamaging True if the obstacle damages the player on contact, false otherwise
     */
    public Obstacle(double x, double y, double width, double height, boolean isDamaging) {
        super(x, y, width, height);
        this.isDamaging = isDamaging;
    }

    /**
     * Checks if this obstacle damages the player on contact.
     * 
     * @return True if the obstacle is damaging, false otherwise
     */
    public boolean isDamaging() {
        return isDamaging;
    }

    /**
     * Updates the obstacle state.
     * This method must be implemented by obstacle classes.
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public abstract void update(double deltaTime);

    /**
     * Renders the obstacle.
     * This method must be implemented by obstacle classes.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public abstract void render(GraphicsContext gc);
}