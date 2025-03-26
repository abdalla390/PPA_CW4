import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Scorpion enemy that patrols horizontally and damages the player on contact.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Scorpion extends Enemy {
    private double patrolDistance = 50.0;
    private double initialX;
    private boolean movingRight = true;

    /**
     * Creates a new scorpion enemy at the specified position.
     * 
     * @param x The x-coordinate of the scorpion
     * @param y The y-coordinate of the scorpion
     */
    public Scorpion(double x, double y) {
        super(x, y, 40, 20);
        this.speed = 50.0;
        this.damage = 1.0f;
        this.initialX = x;
    }

    /**
     * Updates the scorpion's position based on patrol pattern.
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {
        if (!isActive) return;

        if (movingRight) {
            x += speed * deltaTime;
            if (x > initialX + patrolDistance) movingRight = false;
        } else {
            x -= speed * deltaTime;
            if (x < initialX - patrolDistance) movingRight = true;
        }
    }

    /**
     * Renders the scorpion.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#D22F27")); 
        gc.fillOval(x, y, width, height);
        gc.setStroke(Color.web("#D22F27"));
        gc.strokeLine(x + width / 2, y, x + width / 2, y - 15); 
    }

    /**
     * Attacks the player if there is a collision.
     * 
     * @param player The player to attack
     */
    @Override
    public void attack(Player player) {
        if (collidesWith(player)) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}