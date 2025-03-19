import javafx.scene.canvas.GraphicsContext;

public class Snake extends Enemy {
    private double lungeDistance = 80.0;
    private double lungeCooldown = 2.0;
    private double lungeTimer = 0;
    private boolean isLunging = false;

    public Snake(double x, double y) {
        super(x, y, 60, 15); // Width: 60px, Height: 15px
        this.speed = 100.0;  // Medium movement (Level 4-7)
        this.damage = 1;     // Low damage (½ heart, implemented as 1 due to integer system)
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

        gc.setFill(javafx.scene.paint.Color.valueOf("#9ACD32")); // Yellow-green for snake body
        gc.fillRect(x, y, width, height);
        gc.setFill(javafx.scene.paint.Color.valueOf("#556B2F")); // Darker green for head
        gc.fillOval(x + width - 10, y - 5, 15, 15);
    }

    @Override
    public void attack(Player player) {
        if (isLunging && isColliding(player)) {
            player.takeDamage();
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}