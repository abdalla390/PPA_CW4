import javafx.scene.canvas.GraphicsContext;

abstract class GameObject {
    protected double x, y;
    protected double width, height;
    protected double initialX;
    protected boolean isActive;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.width = width;
        this.height = height;
        this.isActive = true;
    }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);

    public boolean collidesWith(GameObject other) {
    if (other == null || !isActive || !other.isActive) return false;
    
    // Calculate intersection area
    double xOverlap = Math.max(0, Math.min(x + width, other.x + other.width) - Math.max(x, other.x));
    double yOverlap = Math.max(0, Math.min(y + height, other.y + other.height) - Math.max(y, other.y));
    double overlapArea = xOverlap * yOverlap;
    
    // If detailed debugging is needed
    if (other instanceof Flag && xOverlap > 0 && yOverlap > 0) {
        System.out.println("Collision with Flag detected! Overlap area: " + overlapArea);
        System.out.println("  This: x=" + x + ", y=" + y + ", w=" + width + ", h=" + height);
        System.out.println("  Flag: x=" + other.x + ", y=" + other.y + ", w=" + other.width + ", h=" + other.height);
    }
    
    // Consider collision valid if there is any overlap
    return xOverlap > 0 && yOverlap > 0;
}
    
    
    
    

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}