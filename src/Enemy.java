import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

public abstract class Enemy extends GameObject {
    protected double speed;
    protected int damage;
    protected boolean isActive;
    protected Random random = new Random();

    public Enemy(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.isActive = true;
    }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);
    public abstract void attack(Player player); // Updated to interact with Player

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        this.isActive = false;
    }

    // Helper method for collision detection
    protected boolean isColliding(Player player) {
        double dx = player.getX() - this.x;
        double dy = player.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (this.width / 2 + player.getWidth() / 2);
    }
}