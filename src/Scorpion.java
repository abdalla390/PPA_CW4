import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Scorpion extends Enemy {
    private double patrolDistance = 50.0;
    private double initialX;
    private boolean movingRight = true;

    public Scorpion(double x, double y) {
        super(x, y, 40, 20);
        this.speed = 50.0;
        this.damage = 1.0f; // High damage (1 heart)
        this.initialX = x;
    }

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

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;

        gc.setFill(Color.web("#D22F27")); // Scorpion red per spec
        gc.fillOval(x, y, width, height);
        gc.setStroke(Color.web("#D22F27"));
        gc.strokeLine(x + width / 2, y, x + width / 2, y - 15); // Tail
    }

    @Override
    public void attack(Player player) {
        if (collidesWith(player)) {
            player.takeDamage(damage);
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}