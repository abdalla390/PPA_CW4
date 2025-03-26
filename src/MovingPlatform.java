import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

/**
 * Represents a moving platform obstacle in the game.
 * Moving platforms travel horizontally back and forth within a defined range,
 * providing both a challenge and a means of vertical traversal for the player.
 * Unlike other obstacles, platforms do not damage the player on contact.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class MovingPlatform extends Obstacle {
    private double moveRange = 200;
    private double moveSpeed = 50;
    private boolean movingRight = true;
    
    private Point2D velocity = new Point2D(moveSpeed, 0);

    /**
     * Constructs a new moving platform with the specified dimensions and position.
     * Moving platforms are non-damaging obstacles by default.
     * 
     * @param x The x-coordinate of the platform's starting position
     * @param y The y-coordinate of the platform's position
     * @param width The width of the platform
     * @param height The height of the platform
     */
    public MovingPlatform(double x, double y, double width, double height) {
        super(x, y, width, height, false);
    }

    /**
     * Updates the platform's position based on its movement pattern.
     * The platform moves horizontally back and forth within its defined range.
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {
        if (!isActive) return;
        
        if (movingRight) {
            x += moveSpeed * deltaTime;
            velocity = new Point2D(moveSpeed, 0);
            if (x > initialX + moveRange) movingRight = false;
        } else {
            x -= moveSpeed * deltaTime;
            velocity = new Point2D(-moveSpeed, 0);
            if (x < initialX) movingRight = true;
        }
    }

    /**
     * Renders the platform with a desert-themed appearance.
     * The platform includes visual details to match the game's desert aesthetic.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Platform base
        gc.setFill(Color.web("#E9C893")); 
        gc.fillRect(x, y, width, height);
        
        // Platform outline
        gc.setStroke(Color.web("#A69185")); 
        gc.setLineWidth(1.5);
        gc.strokeRect(x, y, width, height);
        
        // Platform detail lines
        gc.setStroke(Color.web("#A69185")); 
        gc.setLineWidth(1);
        gc.setLineDashes(3, 2);
        
        // Horizontal line
        gc.strokeLine(x + 5, y + height/2, x + width - 5, y + height/2);
        
        gc.setLineDashes(null);
    }
    
    /**
     * Gets the current velocity of the platform.
     * This is used for platform-player interactions, such as
     * moving the player along with the platform when they stand on it.
     * 
     * @return A Point2D object representing the platform's current velocity
     */
    public Point2D getVelocity() {
        return velocity;
    }
}