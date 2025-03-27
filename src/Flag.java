import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents the completion flag that marks the end of a level.
 * The flag features an animated waving effect to attract player attention.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Flag extends GameObject {
    private double waveTime = 0;
    
    private final double waveSpeed = 2.0;
    
    /**
     * Constructs a new flag at the specified location.
     * The flag has a standardized size according to the game specifications.
     * 
     * @param x The X-coordinate of the flag in the game world.
     * @param y The Y-coordinate of the flag in the game world.
     */
    public Flag(double x, double y) {
        super(x, y, 20, 40); 
    }

    /**
     * Updates the flag's animation state.
     * Advances the wave animation timer, creating a continuous
     * waving motion effect. The animation cycles every 2π seconds.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    @Override
    public void update(double deltaTime) {
        waveTime += deltaTime * waveSpeed;
        if (waveTime > Math.PI * 2) {
            waveTime -= Math.PI * 2;
        }
    }

    /**
     * Renders the flag on the screen.
     * Creates a pole with a waving golden flag attached to it.
     * The flag features a wave effect with sine functions and
     * includes horizontal lines for detail. The flag is outlined
     * in black to improve visibility from a distance.
     * 
     * @param gc The graphics context to draw on.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Draw pole
        gc.setFill(Color.web("#8B4513")); 
        gc.fillRect(x, y, 5, height);
        
        // Draw waving flag
        gc.setFill(Color.GOLD);
        
        // Create a wave effect
        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        
        // Wave amplitude
        double waveAmp = 5.0;
        
        // Top-left corner
        xPoints[0] = x + 5;
        yPoints[0] = y;
        
        // Top-right corner with wave
        xPoints[1] = x + width + Math.sin(waveTime) * waveAmp;
        yPoints[1] = y + Math.sin(waveTime * 0.8) * waveAmp;
        
        // Bottom-right corner with wave
        xPoints[2] = x + width + Math.sin(waveTime + 0.5) * waveAmp;
        yPoints[2] = y + height / 2 + Math.sin(waveTime * 0.8 + 0.5) * waveAmp;
        
        // Bottom-left corner
        xPoints[3] = x + 5;
        yPoints[3] = y + height / 2;
        
        // Draw flag with wave effect
        gc.fillPolygon(xPoints, yPoints, 4);
        
        // Add some flag details
        gc.setStroke(Color.web("#FFD700", 0.7)); 
        gc.setLineWidth(1);
        gc.strokeLine(x + 10, y + 10, x + width - 5, y + 10);
        gc.strokeLine(x + 10, y + 20, x + width - 5, y + 20);
        
        // Make the flag more visible from a distance
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, 4);
    }
}