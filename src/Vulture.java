import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Vulture enemy that swoops vertically to attack the player.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Vulture extends Enemy {
    private double swoopHeight = 150.0;
    private boolean isSwooping = false;
    private double initialY;

    /**
     * Creates a new vulture enemy at the specified position.
     * 
     * @param x The x-coordinate of the vulture
     * @param y The y-coordinate of the vulture
     */
    public Vulture(double x, double y) {
        super(x, y, 50, 30);
        this.speed = 150.0;
        this.damage = 1.0f;
        this.initialY = y;
    }

    /**
     * Updates the vulture's position based on swooping pattern.
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {
        if (!isActive) return;

        if (isSwooping) {
            y += speed * deltaTime;
            if (y > initialY + swoopHeight) isSwooping = false;
        } else {
            y -= speed * deltaTime;
            if (y < initialY) isSwooping = true;
        }
    }

    /**
     * Renders the vulture as a triangular shape.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#8B4513"));
        gc.fillPolygon(
            new double[]{x, x + width / 2, x + width},
            new double[]{y + height, y, y + height},
            3
        );
    }

    /**
     * Attacks the player if there is a collision.
     * 
     * @param player The player to attack
     */
    @Override
    public void attack(Player player) {
        if (collidesWith(player) && player.isActive() && !player.isInvincible()) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}