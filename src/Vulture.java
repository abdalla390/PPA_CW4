import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Vulture extends Enemy {
    private double swoopHeight = 150.0;
    private boolean isSwooping = false;
    private double initialY;

    public Vulture(double x, double y) {
        super(x, y, 50, 30);
        this.speed = 150.0;
        this.damage = 1.0f; // Medium damage (1 heart)
        this.initialY = y;
    }

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

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#8B4513")); // Vulture brown per spec
        gc.fillPolygon(
            new double[]{x, x + width / 2, x + width},
            new double[]{y + height, y, y + height},
            3
        );
    }

    @Override
    public void attack(Player player) {
        // Fixed: Remove the isSwooping check to ensure damage on any collision
        if (collidesWith(player) && player.isActive() && !player.isInvincible()) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}