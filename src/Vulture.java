import javafx.scene.canvas.GraphicsContext;

public class Vulture extends Enemy {
    private double swoopHeight = 150.0; // Matches player's jump height
    private boolean isSwooping = false;
    private double initialY;

    public Vulture(double x, double y) {
        super(x, y, 50, 30); // Width: 50px, Height: 30px
        this.speed = 150.0;  // Medium movement (Level 8-10)
        this.damage = 1;     // Medium damage (1 heart)
        this.initialY = y;
    }

    @Override
    public void update(double deltaTime) {
        if (!isActive) return;

        // Swooping behavior
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

        gc.setFill(javafx.scene.paint.Color.valueOf("#654321")); // Dark brown for vulture
        gc.fillPolygon(
            new double[]{x, x + width / 2, x + width},
            new double[]{y + height, y, y + height},
            3
        ); // Triangle for wings
    }

    @Override
    public void attack(Player player) {
        if (isColliding(player) && isSwooping) {
            player.takeDamage();
            AnimationManager.createEnemyAttackAnimation(this);
        }
    }
}