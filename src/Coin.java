import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a collectible coin in the game world.
 * Coins can be silver or gold with different point values.
 * They feature rotation animation and collection effects.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Coin extends GameObject {
    /**
     * Defines the available types of coins in the game.
     */
    public enum CoinType { 
        //Silver coin with lower value 
        SILVER, 
        //Gold coin with higher value
        GOLD 
    }
    
    private CoinType type;
    private int value;
    private double rotationAngle = 0;
    private boolean collected = false;
    private double disappearTimer = 0;
    
    /**
     * Constructs a new coin at the specified location with the given type.
     * 
     * @param x The X-coordinate of the coin in the game world.
     * @param y The Y-coordinate of the coin in the game world.
     * @param type The type of coin (SILVER or GOLD) which determines its value.
     */
    public Coin(double x, double y, CoinType type) {
        super(x, y, 20, 20); // Small coin size
        this.type = type;
        this.value = (type == CoinType.GOLD) ? 5 : 1; // Gold = 5, Silver = 1
    }
    
    /**
     * Updates the coin's state each frame.
     * Handles rotation animation and fading out after collection.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
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
    
    /**
     * Renders the coin on the screen.
     * Creates a 3D-like rotating coin effect by varying the width based on rotation angle.
     * Applies a fade-out effect when the coin is collected.
     * 
     * @param gc The graphics context to draw on.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        gc.save();
        
        if (collected) {
            double alpha = 1.0 - (disappearTimer / 0.5);
            gc.setGlobalAlpha(alpha);
        }
        
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(rotationAngle);
        
        double scaleFactor = Math.abs(Math.cos(Math.toRadians(rotationAngle))) * 0.5 + 0.5;
        double drawWidth = width * scaleFactor;
        
        if (type == CoinType.GOLD) {
            
            gc.setFill(Color.GOLD);
            gc.setStroke(Color.DARKGOLDENROD);
        } else {
            
            gc.setFill(Color.SILVER);
            gc.setStroke(Color.DARKGRAY);
        }
        
       
        gc.fillOval(-drawWidth / 2, -height / 2, drawWidth, height);
        
        
        gc.setLineWidth(1);
        gc.strokeOval(-drawWidth / 2, -height / 2, drawWidth, height);
        
        if (scaleFactor > 0.7) { 
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
    
    /**
     * Gets the point value of this coin.
     * 
     * @return The point value of the coin.
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the type of this coin (SILVER or GOLD).
     * 
     * @return The coin type.
     */
    public CoinType getType() {
        return type;
    }
    
    /**
     * Marks the coin as collected, triggering its fade-out animation.
     * Does nothing if the coin is already collected.
     */
    public void collect() {
        if (!collected) {
            collected = true;
        }
    }
    
    /**
     * Checks if the coin has been collected.
     * 
     * @return True if the coin has been collected, false otherwise.
     */
    public boolean isCollected() {
        return collected;
    }
}