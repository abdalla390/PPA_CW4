import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Flag extends GameObject {
    public Flag(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 100;
        this.isActive = true;
    }
    
    @Override
    public void update(double deltaTime) {
    }
    
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GOLD);
        gc.fillRect(x, y, width, height);
    }
}