public abstract class Obstacle extends GameObject {
    protected boolean isDamaging;
    
    @Override
    public abstract void update(double deltaTime);
    
    @Override
    public abstract void render(GraphicsContext gc);
}

// Example subclasses
public class Spike extends Obstacle {
    // Spike-specific behavior
}

public class MovingPlatform extends Obstacle {
    // Moving platform behavior
}