public abstract class Enemy extends GameObject {
    protected double speed;
    protected int damage;
    
    @Override
    public abstract void update(double deltaTime);
    
    @Override
    public abstract void render(GraphicsContext gc);
    
    // Common enemy behaviors
}

// Example subclasses
public class GroundEnemy extends Enemy {
    // Ground-specific behavior
}

public class FlyingEnemy extends Enemy {
    // Flying-specific behavior
}