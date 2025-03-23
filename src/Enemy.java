import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

public abstract class Enemy extends GameObject {
    protected double speed;
    protected float damage; // Changed to float for ½ heart support
    protected Random random = new Random();

    public Enemy(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public abstract void attack(Player player);
    public float getDamage() { return damage; }
}