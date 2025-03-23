import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Obstacle extends GameObject {
    protected boolean isDamaging;

    public Obstacle(double x, double y, double width, double height, boolean isDamaging) {
        super(x, y, width, height);
        this.isDamaging = isDamaging;
    }

    public boolean isDamaging() {
        return isDamaging;
    }

    @Override
    public abstract void update(double deltaTime);

    @Override
    public abstract void render(GraphicsContext gc);
}