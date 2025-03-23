import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Snake extends Enemy {
    private double lungeDistance = 80.0;
    private double lungeCooldown = 2.0;
    private double lungeTimer = 0;
    private boolean isLunging = false;

    public Snake(double x, double y) {
        super(x, y, 60, 15);
        this.speed = 100.0;
        this.damage = 0.5f; // Low damage (½ heart)
        this.initialX = x;
    }

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

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#4F7942")); // Snake green per spec
        gc.fillRect(x, y, width, height);
        gc.setFill(Color.web("#2E8B57")); // Darker green head
        gc.fillOval(x + width - 10, y - 5, 15, 15);
    }

    @Override
    public void attack(Player player) {
        if (isLunging && collidesWith(player)) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}