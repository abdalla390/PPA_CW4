import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Coin extends GameObject {
    public enum CoinType { SILVER, GOLD }
    
    private CoinType type;
    private int value;
    private double rotationAngle = 0;
    private boolean collected = false;
    private double disappearTimer = 0;
    
    public Coin(double x, double y, CoinType type) {
        super(x, y, 20, 20); // Small coin size
        this.type = type;
        this.value = (type == CoinType.GOLD) ? 5 : 1; // Gold = 5, Silver = 1
    }
    
    @Override
    public void update(double deltaTime) {
        if (!isActive) return;
        
        // Animate coin rotation
        rotationAngle += deltaTime * 180; // Rotate 180 degrees per second
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        }
        
        // Handle collection animation
        if (collected) {
            disappearTimer += deltaTime;
            if (disappearTimer >= 0.5) { // Disappear after 0.5 seconds
                isActive = false;
            }
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        gc.save();
        
        // Set up coin appearance
        if (collected) {
            // Fade out when collected
            double alpha = 1.0 - (disappearTimer / 0.5);
            gc.setGlobalAlpha(alpha);
        }
        
        // Translate to center of coin for rotation
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(rotationAngle);
        
        // Draw coin (simulating 3D by changing width based on rotation)
        double scaleFactor = Math.abs(Math.cos(Math.toRadians(rotationAngle))) * 0.5 + 0.5;
        double drawWidth = width * scaleFactor;
        
        // Coin color based on type
        if (type == CoinType.GOLD) {
            // Gold coin
            gc.setFill(Color.GOLD);
            gc.setStroke(Color.DARKGOLDENROD);
        } else {
            // Silver coin
            gc.setFill(Color.SILVER);
            gc.setStroke(Color.DARKGRAY);
        }
        
        // Draw the coin
        gc.fillOval(-drawWidth / 2, -height / 2, drawWidth, height);
        
        // Coin outline
        gc.setLineWidth(1);
        gc.strokeOval(-drawWidth / 2, -height / 2, drawWidth, height);
        
        // Coin details (dollar sign or similar)
        if (scaleFactor > 0.7) { // Only show details when coin is mostly facing front
            if (type == CoinType.GOLD) {
                gc.setFill(Color.DARKGOLDENROD);
            } else {
                gc.setFill(Color.DARKGRAY);
            }
            gc.setFont(javafx.scene.text.Font.font(12));
            gc.fillText("$", -4, 4);
        }
        
        gc.restore();
    }
    
    public int getValue() {
        return value;
    }
    
    public CoinType getType() {
        return type;
    }
    
    public void collect() {
        if (!collected) {
            collected = true;
        }
    }
    
    public boolean isCollected() {
        return collected;
    }
}