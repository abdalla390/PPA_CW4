import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

/**
 * Abstract base class for all enemy entities in the game.
 * Defines common properties and behaviors that all enemy types share,
 * while allowing for specialized implementation in subclasses.
 * Enemies can damage the player and have varied movement patterns.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public abstract class Enemy extends GameObject {
    protected double speed;

    protected float damage;
    
    protected Random random = new Random();

    /**
     * Constructs a new enemy at the specified location with the given dimensions.
     * Subclasses must call this constructor and initialize speed and damage values.
     * 
     * @param x The X-coordinate of the enemy in the game world.
     * @param y The Y-coordinate of the enemy in the game world.
     * @param width The width of the enemy's collision box.
     * @param height The height of the enemy's collision box.
     */
    public Enemy(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Defines how this enemy attacks the player.
     * 
     * @param player The player that this enemy attempts to attack.
     */
    public abstract void attack(Player player);
    
    /**
     * Gets the amount of damage this enemy can inflict.
     * 
     * @return The damage value in heart units (can be fractional for partial hearts).
     */
    public float getDamage() { 
        return damage; 
    }
}