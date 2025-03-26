import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Snake enemy that lunges forward to attack the player.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Snake extends Enemy {
    private double lungeDistance = 80.0;
    private double lungeCooldown = 2.0;
    private double lungeTimer = 0;
    private boolean isLunging = false;

    /**
     * Creates a new snake enemy at the specified position.
     * 
     * @param x The x-coordinate of the snake
     * @param y The y-coordinate of the snake
     */
    public Snake(double x, double y) {
        super(x, y, 60, 15);
        this.speed = 100.0;
        this.damage = 1.0f;
        this.initialX = x;
    }

    /**
     * Updates the snake's position and lunge attack behavior.
     * 
     * @param deltaTime Time elapsed since the last update in seconds
     */
    @Override
    public void update(double deltaTime) {
        if (!isActive) return;

        lungeTimer -= deltaTime;
        if (lungeTimer <= 0 && !isLunging) {
            isLunging = true;
            x += lungeDistance;
        } else if (isLunging) {
            x -= speed * deltaTime;
            if (x <= initialX) {
                isLunging = false;
                lungeTimer = lungeCooldown;
            }
        }
    }

    /**
     * Renders the snake with a green body and darker head.
     * 
     * @param gc The graphics context to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#4F7942"));
        gc.fillRect(x, y, width, height);
        gc.setFill(Color.web("#2E8B57"));
        gc.fillOval(x + width - 10, y - 5, 15, 15);
    }

    /**
     * Attacks the player if lunging and there is a collision.
     * 
     * @param player The player to attack
     */
    @Override
    public void attack(Player player) {
        if (isLunging && collidesWith(player)) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}