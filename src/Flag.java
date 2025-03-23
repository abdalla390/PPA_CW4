import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Flag extends GameObject {
    private double waveTime = 0;
    private final double waveSpeed = 2.0; // Speed of flag waving
    
    public Flag(double x, double y) {
        super(x, y, 20, 40); // Standardized to spec size
    }

    @Override
    public void update(double deltaTime) {
        // Update wave animation
        waveTime += deltaTime * waveSpeed;
        if (waveTime > Math.PI * 2) {
            waveTime -= Math.PI * 2;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Draw pole
        gc.setFill(Color.web("#8B4513")); // Brown for pole
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
        gc.setStroke(Color.web("#FFD700", 0.7)); // Slightly transparent gold
        gc.setLineWidth(1);
        gc.strokeLine(x + 10, y + 10, x + width - 5, y + 10);
        gc.strokeLine(x + 10, y + 20, x + width - 5, y + 20);
        
        // Make the flag more visible from a distance
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, 4);
    }
}