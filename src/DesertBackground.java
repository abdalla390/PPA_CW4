import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DesertBackground extends GameObject {
    public DesertBackground(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isActive = true;
    }
    
    @Override
    public void update(double deltaTime) {
    }
    
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BURLYWOOD);
        gc.fillRect(x, y, width, height);
    }
}
