import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DesertBackground {
    private double scrollOffset = 0;

    public void update(double deltaTime) {
        scrollOffset -= 50 * deltaTime; // Slow scroll for parallax
        if (scrollOffset < -1280) scrollOffset = 0; // Loop background
    }

    public void render(GraphicsContext gc) {
        // Sand color background
        gc.setFill(Color.valueOf("#F4A460")); // Sandy brown
        gc.fillRect(0, 0, 1280, 720);

        // Simple dunes (static for now)
        gc.setFill(Color.valueOf("#CD853F")); // Darker sand
        gc.fillOval(scrollOffset, 500, 600, 300);
        gc.fillOval(scrollOffset + 800, 550, 500, 200);
    }
}