import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DesertBackground extends GameObject {
    public DesertBackground(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double deltaTime) {
        // Static background, no updates needed
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        // Sky
        gc.setFill(Color.web("#87CEEB")); // Sky blue
        gc.fillRect(x, y, width, height * 0.86); // Sky takes up 86% of the height
        
        // Ground
        gc.setFill(Color.web("#E9C893")); // Sand color
        gc.fillRect(x, y + height * 0.86, width, height * 0.14); // Ground is the bottom 14%
        
        // Ground line - make it clear where the ground level is
        gc.setStroke(Color.web("#A69185")); // Darker line for ground
        gc.setLineWidth(2);
        gc.strokeLine(x, y + height * 0.86, x + width, y + height * 0.86);
        
        // Add some details to the background
        // Distant mountains
        gc.setFill(Color.web("#808080", 0.5)); // Light gray mountains
        gc.fillPolygon(
            new double[]{x + width * 0.1, x + width * 0.2, x + width * 0.3},
            new double[]{y + height * 0.86, y + height * 0.7, y + height * 0.86},
            3
        );
        
        gc.fillPolygon(
            new double[]{x + width * 0.5, x + width * 0.7, x + width * 0.9},
            new double[]{y + height * 0.86, y + height * 0.65, y + height * 0.86},
            3
        );
    }
}