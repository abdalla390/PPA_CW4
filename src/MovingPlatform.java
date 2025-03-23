import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

public class MovingPlatform extends Obstacle {
    private double moveRange = 200;
    private double moveSpeed = 50;
    private boolean movingRight = true;
    private Point2D velocity = new Point2D(moveSpeed, 0);

    public MovingPlatform(double x, double y, double width, double height) {
        super(x, y, width, height, false);
    }

    @Override
    public void update(double deltaTime) {
        if (!isActive) return;
        
        if (movingRight) {
            x += moveSpeed * deltaTime;
            velocity = new Point2D(moveSpeed, 0);
            if (x > initialX + moveRange) movingRight = false;
        } else {
            x -= moveSpeed * deltaTime;
            velocity = new Point2D(-moveSpeed, 0);
            if (x < initialX) movingRight = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Platform base
        gc.setFill(Color.web("#E9C893")); // Light tan
        gc.fillRect(x, y, width, height);
        
        // Platform outline
        gc.setStroke(Color.web("#A69185")); // Gray-brown outline
        gc.setLineWidth(1.5);
        gc.strokeRect(x, y, width, height);
        
        // Platform detail lines
        gc.setStroke(Color.web("#A69185")); // Gray-brown
        gc.setLineWidth(1);
        gc.setLineDashes(3, 2);
        
        // Horizontal line
        gc.strokeLine(x + 5, y + height/2, x + width - 5, y + height/2);
        
        // Reset line style
        gc.setLineDashes(null);
    }
    
    public Point2D getVelocity() {
        return velocity;
    }
}