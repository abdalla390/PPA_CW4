import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * Represents the static desert background of the game world.
 * Draws the sky, ground, distant mountains, and ground line to create
 * a cohesive desert environment for the game.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */

public class DesertBackground extends GameObject {
    /**
     * Constructs a new desert background that covers the specified area.
     * 
     * @param x The X-coordinate of the background in the game world.
     * @param y The Y-coordinate of the background in the game world.
     * @param width The width of the background area.
     * @param height The height of the background area.
     */
    
    public DesertBackground(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    
     /**
     * Updates the background state.
     * The background is static, so this method doesn't perform any actions.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    @Override
    public void update(double deltaTime) {
        // Static background, no updates needed
    }

    /**
     * Renders the desert background on the screen.
     * Creates a layered environment with:
     * - A sky-blue upper section (86% of height)
     * - A sand-colored ground section (14% of height)
     * - A dividing line where the ground meets the sky
     * - Distant mountain silhouettes for depth and atmosphere
     * 
     * @param gc The graphics context to draw on.
     */
    
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Sky
        gc.setFill(Color.web("#87CEEB")); // Sky blue
        gc.fillRect(x, y, width, height * 0.86); // Sky takes up 86% of the height
        
        // Ground
        gc.setFill(Color.web("#E9C893")); // Sand color
        gc.fillRect(x, y + height * 0.86, width, height * 0.14); // Ground is the bottom 14%
        
        // Ground line - make it clear where the ground level is
        gc.setStroke(Color.web("#A69185")); // Darker line for ground
        gc.setLineWidth(2);
        gc.strokeLine(x, y + height * 0.86, x + width, y + height * 0.86);
        
        // Add some details to the background
        // Distant mountains
        gc.setFill(Color.web("#808080", 0.5)); // Light gray mountains
        gc.fillPolygon(
            new double[]{x + width * 0.1, x + width * 0.2, x + width * 0.3},
            new double[]{y + height * 0.86, y + height * 0.7, y + height * 0.86},
            3
        );
        
        gc.fillPolygon(
            new double[]{x + width * 0.5, x + width * 0.7, x + width * 0.9},
            new double[]{y + height * 0.86, y + height * 0.65, y + height * 0.86},
            3
        );
    }
}