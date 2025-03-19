import javafx.scene.canvas.GraphicsContext;

public class Scorpion extends Enemy {
    private double patrolDistance = 100.0;
    private double initialX;
    private boolean movingRight = true;

    public Scorpion(double x, double y) {
        super(x, y, 40, 20); // Width: 40px, Height: 20px
        this.speed = 50.0;   // Slow movement (Level 1-3)
        this.damage = 1;     // High damage (1 heart)
        this.initialX = x;
    }

    @Override
    public void update(double deltaTime) {
        if (!isActive) return;

        // Patrol behavior
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

        // Desert-themed colors
        gc.setFill(javafx.scene.paint.Color.valueOf("#8B4513")); // Brown for scorpion body
        gc.fillOval(x, y, width, height);
        gc.setStroke(javafx.scene.paint.Color.valueOf("#8B4513"));
        gc.strokeLine(x + width / 2, y, x + width / 2, y - 15); // Tail
    }

    @Override
    public void attack(Player player) {
        if (isColliding(player)) {
            player.takeDamage();
            AnimationManager.createEnemyAttackAnimation(this); // Trigger attack animation
        }
    }
}