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
        gc.setFill(Color.web("#87CEEB")); 
        gc.fillRect(x, y, width, height * 0.86);
        
        // Ground
        gc.setFill(Color.web("#E9C893")); 
        gc.fillRect(x, y + height * 0.86, width, height * 0.14); 
        
        gc.setStroke(Color.web("#A69185")); 
        gc.setLineWidth(2);
        gc.strokeLine(x, y + height * 0.86, x + width, y + height * 0.86);
        
        // First sand dune
        gc.setFill(Color.web("#D4B483", 0.7)); 
        double dune1X = x + width * 0.1;
        double dune1Width = width * 0.3;
        double dune1Height = height * 0.15;
        drawSandDune(gc, dune1X, y + height * 0.86 - dune1Height, dune1Width, dune1Height);
        
        // Second sand dune 
        gc.setFill(Color.web("#E9C893", 0.6)); 
        double dune2X = x + width * 0.5;
        double dune2Width = width * 0.4;
        double dune2Height = height * 0.2;
        drawSandDune(gc, dune2X, y + height * 0.86 - dune2Height, dune2Width, dune2Height);
        
        // Third smaller dune
        gc.setFill(Color.web("#C19A6B", 0.5)); 
        double dune3X = x + width * 0.35;
        double dune3Width = width * 0.2;
        double dune3Height = height * 0.1;
        drawSandDune(gc, dune3X, y + height * 0.86 - dune3Height, dune3Width, dune3Height);
    }
    
    private void drawSandDune(GraphicsContext gc, double x, double y, double width, double height) {
        // Use bezier curves for a smoother, more natural dune shape
        gc.beginPath();
        gc.moveTo(x, y + height); 
        
        // Create a curve for the dune
        gc.bezierCurveTo(
            x + width * 0.3, y + height * 0.5,
            x + width * 0.7, y + height * 0.5, 
            x + width, y + height              
        );
        
        // Complete the shape
        gc.lineTo(x + width, y + height);
        gc.lineTo(x, y + height);
        gc.closePath();
        gc.fill();
        
        // Add a subtle highlight line to give some depth
        gc.setStroke(Color.web("#F5DEB3", 0.3)); 
        gc.setLineWidth(1);
        
        // Draw a subtle curve near the top of the dune for highlight
        gc.beginPath();
        gc.moveTo(x + width * 0.1, y + height * 0.7);
        gc.quadraticCurveTo(
            x + width * 0.5, y + height * 0.4,
            x + width * 0.9, y + height * 0.7
        );
        gc.stroke();
    }
    
    
}