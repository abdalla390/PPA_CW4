public abstract class GameObject {
    protected double x, y;
    protected double width, height;
    protected boolean isActive;
    
    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);
    
    public boolean collidesWith(GameObject other) {
        // Collision detection logic
    }
    
    // Getters and setters
}